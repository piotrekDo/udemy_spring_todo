# ORM i Hibernate

Hibernate do działania wykorzystuje *EntityManager* do komunikacji z bazą danych.

### Adnotacja @Column

Adnotacja @Column pozwala na zdefiniowanie kluczowych ustawień dla danch, takich jak unikalność czy wymóg przekazania 
jakiejś wartośći (*not null*). Warto zwrócić szczególną uwagę na opcję *columnDefinition* przyjmującą *String* pozwalający
zapisać dowolne ustawienie w SQL, np.
```
@Entity
public class User {
    @Id
    Long id;

    @Column(columnDefinition = "varchar(255) default 'John Snow'")
    private String name;

    @Column(columnDefinition = "integer default 25")
    private Integer age;

    @Column(columnDefinition = "boolean default false")
    private Boolean locked;
}
```

### Adnotacja @Transient

Adnotacja wykorzystywana w przypadku pól, których nie chcemy zapisywać do bazy danych, ale chcemy aby było uwzględnione
w JSON. Przykładem może być data urodzenia wyliczana z numeru PESEL przechowywanego w bazie danych, w tej sytuacji nie
potrzebujemy w bazie dodatkowej kolumny dla daty urodzenia. 

### Adnotacja @PrePersist

Adnotacja nadawana na metody uruchamiane przed zapisem danych- *insert*. 

### Adnotacja @PreUpdate

Adnotacja nadawana na metody uruchamiane przed uaktualnieniem danych.

**Istnieją inne warjacje metod *Pre* oraz *Post* dla innych poleceń bazodanowych np. *@PreDelete***

### Adnotacja @Transactional

Transactional pozwala na wprowadzenie do bazy danych zmian na zarządanym przez Hibernate obiekcie. Nie musimy zapisywać
w kodzie dodatkowych metod na zapis zmian. Transakcja zostaje przerwana w momencie wystąpienia wyjątku obsługiwanego.
Zachowaniem tym można manipulować poprzez [dodatkowe ustawienia](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction-declarative-attransactional-settings)&#x00B9;
możemy na przykład zdefiniować rodzaj wyjątków przerywających działanie transakcji poprzez atrybut *rollbackFor*.
**Metoda na której wykonujemy @Transactional musi być publiczna i musi być częścią Springowego *Bean'a* a także musi
zostać wywołana z innego *bean'a* Springowego**


## Dziedziczenie w Hibernate






<br></br>
<br></br>
<br></br>
### Linki
&#x00B9; Dodatkowe ustawienia dla adnotacji @Transactional  
https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction-declarative-attransactional-settings
