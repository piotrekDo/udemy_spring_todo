# Wstrzykiwanie zależności w Spring

Wstrzykiwanie zależności to jedna z głównych funkcjonalności Spring, odpowiada za nią *kontener IoC*. Dzięki temu
jesteśmy zwolnieni z konieczności samodzielnego tworzenia obiektów.

## Bean

Bean to obiekt, kórego cykl życia jest zarządzany przez Spring. Istnieje kilka sposobów zarejestrowania obiektu w
kontenerze IoC. Możemy do tego wykorzystać:
- konfigurację w pliku xml.
- odpowiednią adnotację (tzw. stereotyp).
- wykorzystać  klasę konfiguracyjną z metodami oznaczonymi adnotacją``@Bean``.
  
**Zbiór wszystkich beanów w aplikacji często nazywany jest *kontekstem***  

### Stereotypy

Stereotypami nazywamay adnotacje umożliwiające utworzenia Beana z klasy. Istnieją między nimi pewne różnice
- ``@Component`` oznacza generyczny komponent.
- ``@Service`` funkcjonalnie nie różni się niczym od ``@Component``. Jest jedynie informacją, że klasa ta reprezeuntuje
warstwę serwisową.
- ``@Repository`` reprezentuje warstwę dostępu do bazy danych. Od adnotacji ``@Component`` różni się jedynie tym, że
w przypadku wystąpienia błędu na warstwie bazodanowej możemy otrzymać bardziej szczegółowe informacje o wyjątkach. 
- ``@Controller`` działa tak jak ``@Component``, ale bean nad którym znajduje się ta adnotacja trafia również do 
``WebAplicationContext``, tzn. do tej części kontekstu reprezentującej obiekty tworzące wartwę webową aplikacji. 

### @Configuration oraz @Bean

*Stereotypy* pozwalają na zdefiniowanie klasy jako bean. Czasami jednak potrzebujemy zdefiniować kilka powiązanych ze sobą
beanów. W takim przypadku można umieścić ich definicje w pojedyńczej klasie. Klasa taka powinna być oznaczona adnotacją
``@Configuration``. W definicji tej klasy znajdą się metody **zwracające instancje obiektów**, które trafią do kontekstu.
Każda z takich metod musi zostać oznaczona adnotacją ``@Bean`` właśnie. Obiekty zwracane w metodach oznaczonych ``@Bean``
tworzymy 'ręcznie', tzn. np. za pomocą słówka ``new`` lub przy pomocy wzorców konstrukcyjnych. 

***UWAGA!** Klasa oznaczona adnotacją ``@Configuration`` również **jest** komponentem*.

## Inne ważne adnotacje

### @SpringBootAppllication
Adnotacja zapewniająca kilka funkcjonalności. Wynikają one z tego, że sama adnotacja ``@SpringBootApplication`` składa 
się z kilku innych:
- ``@SpringBootConfiguration``
- ``@EnableAutoConfiguration``
- ``@ComponentScan``
  
``@SpringBootConfiguration`` działa tak samo jak ``@Configuration``. Oznacza to, że w jej wnętrzu możemy definiować beany.  
``@EnableAutoConfiguration`` włącza mechanizm automatycznej konfiguracji. Dzięki jej istnieniu aplikacja tworzy pewien 
kontekst na podstawie konfiguracji i zależności w pliku ``pom.xml``.  
``@ComponentScan`` informuje w jakich pakietach powinny być szukane beany, które trafią do kontekstu. Domyślnie adnotacja 
ta w poszukiwaniu komponentów skanuje aktualny pakiet i pakiety w nim zawarte.  
To oznacza, że dla klasy oznaczonej adnotacją ``@SpringBootApplication`` w pakiecie ``com.example.a`` do kontekstu trafią
wszysystkie klasy, których nazwa pakietu rozpoczyna się od ``com.example.a.`` ale klasa zawarta w pakicie ``com.example``
lub np. ``com.example.b`` już nie.  
Problemy te możemy rozwiązać dodając właśnie adnotację ``@ComponentScan`` nad dowolną klasę oznaczoną jako ``@Configuration``
a więc rówwnież ``@SpringBootApplication``. W polu ``basePackages`` możemy wskazać pakiety, które powinny zostać użyte do 
szukania innych beanów. 
```
@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class SbApplication {

  public static void main(String[] args) {
    SpringApplication.run(SbApplication.class, args);
  }
}
```