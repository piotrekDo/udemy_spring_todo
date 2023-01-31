# ORM i Hibernate

Hibernate do działania wykorzystuje *EntityManager* do komunikacji z bazą danych.

### Adnotacja @Column

Adnotacja @Column pozwala na zdefiniowanie kluczowych ustawień dla danch, takich jak unikalność czy wymóg przekazania
jakiejś wartośći (*not null*). Warto zwrócić szczególną uwagę na opcję *columnDefinition* przyjmującą *String*
pozwalający
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
Zachowaniem tym można manipulować
poprzez [dodatkowe ustawienia](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction-declarative-attransactional-settings)
&#x00B9;
możemy na przykład zdefiniować rodzaj wyjątków przerywających działanie transakcji poprzez atrybut *rollbackFor*.
**Metoda na której wykonujemy @Transactional musi być publiczna i musi być częścią Springowego *Bean'a* a także musi
zostać wywołana z innego *bean'a* Springowego**

## Dziedziczenie w Hibernate

Do oznaczania klas po których dziedziczymy używamy adnotacji **@MappedSuperclass**. Klasy z tą adnotacją nie znajdą
odzwierciedlenia w bazie danych. Następnie w klasie dziedziczącej korzystamy z adnotacji **@Inheritance**. Przekazujemy
do niej enum *InheritanceType* z trzema opcjami do wyboru:

- JOINED - każda encja posiada własną tabelę, z wydzieloną częścią wspólną do odobnej tabeli, wymaga joinów.
- SINGLE_TABLE - encje z różnych klas ze wspólną klasą bazową są umieszczane w jednej tabeli.
- TABLE_PER_CLASS - każda encja ma osobną tabelę, wliczając wspólne cechy, niepotrzebne są joiny.

Strategia **SINGLE_TABLE** jest domyślną jeżeli nie wskażemy inaczej. W przypadku pojedyńczej tabeli zostanie utworzona
dodatkowa kolumna pozwalająca rozróznić przechowywane rekordy, domyślnie nazwana *DTYPE*. Możemy to jednak zmienić
poprzez adnotację **@DiscriminatorColumn**
```
@Entity(name="products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="product_type", 
  discriminatorType = DiscriminatorType.INTEGER)
public class MyProduct {
    // ...
}
```
Wówczas w klasach pochodnych musimy ustalić wartość dla kolumny *product_type*
```
@Entity
@DiscriminatorValue("1")
public class Book extends MyProduct {
    // ...
}

@Entity
@DiscriminatorValue("2")
public class Pen extends MyProduct {
    // ...
}
```
  
W przypadku **JOINED** otrzymamy tabelę nadrzędną z częścią wspólną oraz osobne tabele dla każdej z encji. Jedyną rzeczą
powtarzaną w tabelach pochodnych jest ID jako klucz obcy nawiązujący do rekordu z tabeli nadrzędnej aby umożliwić
Hibernate wykonanie join'a. Nazwę tej kolumny możemy modyfikować dzięki adnotacji **@PrimaryKeyJoinColumn**
```
@Entity
@PrimaryKeyJoinColumn(name = "petId")
public class Pet extends Animal {
    // ...
}
```

Minusem tego rozwiązania jest konieczność wykonania join'ów przy każdym select, zwłaszcza w przypadku klasy nadrzędnej
konieczny będzie join z tabelą każdej dziedziczącej encji co może się negatywnie odbić na wydajnośći aplikacji.  
  
Ostatnim rozwiązaniem jest **TABLE_PER_CLASS** tworzące osobną tabelę dla każdej dziedziczącej encji czego minusem jest
duplikowanie części wspólnej dla każdej encji. Kolejnym minusem jest **konieczność zrezygnowania ze** strategii generowania
ID na podstawie **GenerationType.IDENTITY**.

**[Więcej o strategiach generowania kluczy ID](https://thorben-janssen.com/jpa-generate-primary-keys/)** &#x00B2;

## Embeddable / Embedded
Adnotacja @Embaddable pozwala na stosowanie kompozycji/ agregacji, czyli umieszczaniu jednego obietu wewnątrz drugiego.
**@Embeddable umieszczane jest w klasie agregowanej**, nie musi być ona abstrakcyjna i nie jest osobno przechowywana w bazie
danycyh. **Adnotacji @Embeddable nie można łączyć z @Entity**, klasy te nie są osobno mapowane do bazy danych.  
  
**W klasie agregującej stosujemy adnotację @Embedded nad polami zagregowanymi**   
                                            
<br></br>
**Zmiany wprowadzone przy użyciu adnotacji @MappedSuperclass oraz @Embedded nie wymagają migracji!**



<br></br>
<br></br>
<br></br>

### Linki

&#x00B9; Dodatkowe ustawienia dla adnotacji @Transactional  
https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction-declarative-attransactional-settings  
&#x00B2; Strategie generowania kluczy głównych w Hibernate  
https://thorben-janssen.com/jpa-generate-primary-keys/  

