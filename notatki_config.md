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

W dokumentacji Spring możemy znaleźć listę często występujących konfiguracji wraz z ich opisem
https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html

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

