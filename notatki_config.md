# Konfiguracja, properties, profile Springa

Wszelkie konfiguracje wprowadzmy w pliku *.properties* lub w pliku wpierającym format *YAML*. 
Zamiany zapisujemy w postaci par klucz - wartość. Każde kolejne ustawienie zapisujemy rozdzielając enterem,
bez przecinków, sredników czy iinnych separatorów. Wyrażenia String zapisujemy bez cudzysłowu, np.
>spring.h2.console.enabled=true  
>spring.h2.console.path=/h2  

Powyższe ustawienia pozwalają na podgląd do bazy danych H2 oraz zmieniają jej adres na *localhost:8080/h2*

Bazę danych H2 możemy też zapisywać do pliku. w konfiguracji podajemy adres url jako file
> ``` spring.datasource.url=jdbc:h2:file:./todo-db ```  
gdzie todo-db to nazwa pliku, który zostanie utworzony.  
W tym miejscu warto wspomnieć o poleceniu  
> ``` spring.jpa.hibernate.ddl-auto= ```  
pozwalającym ustalić stworzenie, czy walidację istniejącej bazy.

W [dokumentacji](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)&#x00B9; Spring możemy znaleźć listę często występujących konfiguracji wraz z ich opisem


## YAML
Yaml jest innym formatem zapisu, pozwalającym zredukować ilość kodu i potencjalnie uczynić kod bardziej
przejrzystym. Do zapisu w formacie YAML uzywamy plików z rozszerzeniem .yml.
Całość zapisu oparta jest na wcięciach przez spację, z dwukropkiem rozdzilającym kolejne
segmenty. W YAML String podajemy w 'text'.
>spring:  
&emsp; h2:  
&emsp;&emsp; console:  
&emsp;&emsp;&emsp; enabled: true  
&emsp;&emsp;&emsp; path: /h2


## Wykorzystywanie propsów w kodzie

#### @Value("some_value")
Można wykorzystać adnotację @Value przyjmującą jego ścieżkę. Adnotację zapisujemy bezpośrednio nad zmienną
w dolarach i klamrach *"${my.prop}"*
> ~~~
> @Value("${spring.datasource.url}")  
> private String url; 
> ~~~

#### Wykorzystanie wstrzykiwania zależności

Każdy props ma swój odpowiednik w postaci klasy w Spring. Do tych obiektów możemy się odwoływać.
Możemy te obiekty wstrzykiwać do naszych klas. W takiej sytuacji potencjalne błędy zostaną wychwycone przez kompilator. 

```
    private final DataSourceProperties dataSourceProperties;

    public InfoController(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @GetMapping("/url")
    String url() {
        return dataSourceProperties.getUrl();
    }
```

### Tworzenie własnych klas property

Tworzymy klasę oznaczoną adnotacją @ConfigurationProperties. Wymaga ona przekazania prefiksu po którym występować będą 
odpowiednie właściwości. Właściwości te są odzwierciedlone polami klasy. Wszystkie muszą posiadać gettery oraz settery.

Dawniej dodatkowo taką klasę należało wskazać w innej, oznaczonej pochodnie adnotacją @Configuration do klas 
konfiguracyjnych rozpoznawalnych przez Spring. Wykorzystujemy do tego adnotację @EnableConfigurationProperties(NazwaKlasy.class)

dla przykładu utworzymy klasę konfiguracji dla 'task':

```
@ConfigurationProperties("task")
public class TaskConfigurationProperties {

    private boolean allowMultipleTasksFromTemplate;

    public boolean isAllowMultipleTasksFromTemplate() {
        return allowMultipleTasksFromTemplate;
    }

    public void setAllowMultipleTasksFromTemplate(boolean allowMultipleTasksFromTemplate) {
        this.allowMultipleTasksFromTemplate = allowMultipleTasksFromTemplate;
    }
}
```

Którą należało dodać np. w klasie zawierającą adnotację @SpringBootApplication (adnotacja ta pochodzi od @Configuration)

```
@EnableConfigurationProperties(TaskConfigurationProperties.class)
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}
```

Innym rozwiązaniem tej sytuacji jest nadanie adnotacji @Configuration bezpośrednio w klasie zawierającej adnotację
@ConfigurationProperties, czyli w naszym przypadku w TaskConfigurationProperties.

**Od wersji Spring 2.2 nie jest to wymagane, pomimo to InteliJ nadal sygnalizuje problem.**

### Spring Boot Configuration Processor
Kolejną kwestją jest sygnalizowany przez InteliJ brak Spring Boot Configuration Annotation Processor'a.
Z uwagi na brak tej zależności nie dostaniemy podpowiedzi w plikach .properties czy .yml nawet jeżeli utworzymy 
odpowiednie klasy odpowiadające za konfigurację. Aby uzyskać Spring Boot Configuration Processor powinniśmy dodać
kolejną zależność do pliku pom.xml:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

W pewnych sytuacjach możemy w dalszym ciągu nie uzyskiwać podpowiedzi w plikach .properties oraz .yml. W takim przypadku
może pomóc reset InteliJ wraz z wyczyszczeniem cache. File -> Invalidate Caches.

**Jeżeli aplikacja nie chce się zbudować możliwe jest, że wymagane są kroki opisane w sekcji *Tworzenie własnych klas property*,
a więc umieszczenie odpowiednich adnotacji z włączeniem naszej klasy w klasie zawierającą adnotację @Configuration.
innym jeszcze rozwiązaniem wprowadzonym w Spring 2.2.1 jest adnotacja @ConfigurationPropertiesScan**

## Profile w Spring
Spring pozwala na tworzenie wielu plików z rozszerzeniem .properties czy .yml, co nazywamy *profilami.* Pozwala to na
wprowadzenie większej elastyczności i manipulowanie wartościami zapisanymi w plikach konfiguracynych. Powszechną praktyką
są odrębne ustawienia dla środowiska produkcyjnego oraz deweloperskiego gdzie możemy na przykład zmienić adres bazy danych
aby móc bezpiecznie wprowadzać oraz testować zmiany. Pliki mogą ze sobą 'współpracować' w taki sposób, że w najogólniejszym
configu pozostawimy niezmienne rzeczy a kolejne profile będą do niego dopisywały swoje zmiany.

Można więc utworzyć hierarchię:
application.yml z podstawowymi ustawieniami:
```
spring:
  main:
    banner-mode: off
  profiles:
    active: 'local'   <-- profil domyślny, czytaj poniżej    
task.template.allowMultipleTasks: false

```

następnie application-local.yml dopisujący ustawienia odpowiednia dla wprowadzania zmian:
```
spring:
  h2.console:
      enabled: true
      path: '/h2'
  datasource:
    url: 'jdbc:h2:file:./todo-db'
    username: 'sa'
```

oraz application-prod.yml ze swoją partią ustawień odpowiednią dla produkcji. Zmiany wprowadzone w *application-local.yml*
nie będą tutaj uwzględniane i *vice versa*
```
spring:
  h2.console:
    enabled: false
    path: '/h2'
  datasource:
    url: 'jdbc:h2:todo-db'
    username: 'sa'
```

### Uruchamienie aplikacji z odpowiednim profilem
**Domyślnie** uruchamia się jedynie plik ***applccation.*** Możemy do tego dodać nasz plik profilu.
w pliku *application.* dopisujemy wartość *spring.profiles.active* wskazującą na domyślny profil.
```
spring.profiles.active=local
```
lub dla *YAML*
```
spring:
  profiles:
    active: 'local'
```

### Nadpisywanie wartośći 

Priorytet dla wartości w pliku konfiguracyjnym:
1. Ustawienia globalne w devtools *$HOME/.config/spring-boot* jeżeli *devtools* są aktywne
2. Property zapisane w testach jednostkowych
3. Argumenty z linii poleceń przy uruchammianiu aplikacji z poziomu terminalu
4. Spring application JSON
5. Parametry z *ServletConfig*
6. Parametry z *ServletContext*
7. [JNDI](https://pl.wikipedia.org/wiki/Java_Naming_and_Directory_Interface)&#x00B2;
8. Java System properties (```System.getProperties()```)
9. Zmienne systemu operacyjnego
10. application.properties / .yml spoza pliku jar
11. application.properties / .yml wewnątrz pliku jar
12. Wartośći pochodzące z adnotacji *@PropertySource* wewnątrz klas oznaczonych adnotacją *@Configuration*
13. Wartośći domyślne, ustawiane przez Spring'a


Istnieje możliwość nadpisywania wartośći z poziomu InteliJ w *EditConfigurations* w polu *ActiveProfiles*
W tym samym miejscu w *Enviroment* -> *VM options* możemy dopisywać poszczególne wartośći z dopiskiem **-D**
```
-Dspring.profiles.active=super-profil
```

jest to zapis nadrzędny nad *ActiveProfiles.*

Istnieje również możliwość zmiennych systemowych- *Enviroment variables*  
Tutaj zapisujemy properties bez przedrostka -D, oraz z separatorem _ zamiast .
```
spring_profiles_active=super-profil222
```

Jest to szczególnie użyteczne chcąc stworzyć skrypt budujący aplikację, np. dla Docker'a.


### Bootstrap.properties / .yml
W aplikacjiach Spring Cloud spotyka się plik *bootstrap.*. Jest to plik z konfiguracjami pobieranymi z serwera 
konfiguracyjnego jeszcze przed wczytaniem plików *.application*. Plik *bootstrap.* może na przykład pobierać 
informacje dotyczące bazy danych. Sam plik zawiera jedynie dane na temat serwera oraz klucz weryfikowny przez serwer
konfiguracyjny. 





LINKI

&#x00B9; Dokumentacja Springa, lista najczęściej występujących props  
https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html  
&#x00B2; JNDI  
https://pl.wikipedia.org/wiki/Java_Naming_and_Directory_Interface