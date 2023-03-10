# ORM i Hibernate

Hibernate do działania wykorzystuje *EntityManager* do komunikacji z bazą danych.

### Adnotacja @Column

Adnotacja ```@Column``` pozwala na zdefiniowanie kluczowych ustawień dla danch, takich jak unikalność czy wymóg przekazania
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

**Istnieją inne warjacje metod *Pre* oraz *Post* dla innych poleceń bazodanowych np. *```@PreDelete```***

### Adnotacja @Transactional

Transactional pozwala na wprowadzenie do bazy danych zmian na zarządanym przez Hibernate obiekcie. Nie musimy zapisywać
w kodzie dodatkowych metod na zapis zmian. Transakcja zostaje przerwana w momencie wystąpienia wyjątku obsługiwanego.
Zachowaniem tym można manipulować
poprzez [dodatkowe ustawienia](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction-declarative-attransactional-settings)
&#x00B9;
możemy na przykład zdefiniować rodzaj wyjątków przerywających działanie transakcji poprzez atrybut *rollbackFor*.
**Metoda na której wykonujemy ```@Transactional``` musi być publiczna i musi być częścią Springowego *Bean'a* a także musi
zostać wywołana z innego *bean'a* Springowego**

## Dziedziczenie w Hibernate

Do oznaczania klas po których dziedziczymy używamy adnotacji **```@MappedSuperclass```**. Klasy z tą adnotacją nie znajdą
odzwierciedlenia w bazie danych. Następnie w klasie dziedziczącej korzystamy z adnotacji **```@Inheritance```**. Przekazujemy
do niej enum *InheritanceType* z trzema opcjami do wyboru:

- JOINED - każda encja posiada własną tabelę, z wydzieloną częścią wspólną do odobnej tabeli, wymaga joinów.
- SINGLE_TABLE - encje z różnych klas ze wspólną klasą bazową są umieszczane w jednej tabeli.
- TABLE_PER_CLASS - każda encja ma osobną tabelę, wliczając wspólne cechy, niepotrzebne są joiny.

Strategia **SINGLE_TABLE** jest domyślną jeżeli nie wskażemy inaczej. W przypadku pojedyńczej tabeli zostanie utworzona
dodatkowa kolumna pozwalająca rozróznić przechowywane rekordy, domyślnie nazwana *DTYPE*. Możemy to jednak zmienić
poprzez adnotację **```@DiscriminatorColumn```**
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
Hibernate wykonanie join'a. Nazwę tej kolumny możemy modyfikować dzięki adnotacji **```@PrimaryKeyJoinColumn```**
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
  
**W klasie agregującej stosujemy adnotację ```@Embedded``` nad polami zagregowanymi**   
                                            
<br></br>
**Zmiany wprowadzone przy użyciu adnotacji ```@MappedSuperclass``` oraz ```@Embedded``` nie wymagają migracji!**

## Relacje bazodanowe
Relacje (związki) umożliwiają zagnieżdżanie jednej encji wewnątrz drugiej. Istnieją trzy rodzaje związków określających 
relację między obiektami.  

- @OneToOne
- @OneToMany / @ManyToOne
- @ManyToMany
  
Zapisując adnotacje ```@OneToMany``` lub ```@ManyToOne``` odnosimy się do klasy w której umieszczamy adnotację.
To oznacza, że umieszczając ```@ManyToOne``` w klasie 'A' nad obiektem 'B' informujemy Hibernate, że chcemy utworzyć związek,
gdzie wiele obiektów 'A' jest powiązanych z jednym obiektem 'B'.  

Wewnątrz adnotacji możemy zarządzać róznymi właściwościami, np. *[Cascade](https://www.baeldung.com/jpa-cascade-types)*&#x00B3; pozwalającym wykonywać operacje na powiązanych 
obiektach

#### MappedBy- relacja dwukierunkowa.
Parametr *MappedBy* może zostać umieszczony wewnątrz adnotacji relacyjnej i wskazuje na **nazwę pola** w klasie, która jest
**właścicielem** relacji (np. posiada referencję do drugiej tabeli za pomocą klucza obcego). Zatem umieszczamy ją w klasie 
nie będącej włącicielem. 
  
**Relacje jeden do wielu / wiele do jednego**  
Relacje tego rodzaju możemy tworzyć na dwa sposoby- za pomocą osobnej tabeli złączeniowej oraz poprzez wskazanie 
właściciela relacji właśnie. 

#### @JoinColumn
Adnotacja umieszczana bezpośrednio pod adnotacją ```@OneTomany``` związek (relację), umożliwiająca określenie nazwy kolumny złączeniowej.
**Może zostać użyta dla określenia relacji jednostronnej**.  
**Sama adnotacja ```@JoinColumn``` również wskazuje właściciela relacji**, przy czym umieszczana jest po stronie właściciela.

```
@ManyToOne
@JoinColumn(name = "task_group_id")
private TaskGroup taskGroup;
```

#### FetchType
FetchType jest ustawieniem przekazywanm do adnotacji relacynej. Określa on czy powiązana kolekcja ma zostać pobrana 
w momencie tworzenia obiektu, który tę kolekcję agreguje czy dopiero w sytuacji gdy jasno na nią wskzujemy, np. poprzez
wywołanie gettera.  
**Dla relacji @OneToMany domyślnym typem jest FetchType.LAZY**

#### Problem zapytań n+1
Problem n+1 select'ów dotyczy sytuacji gdzie operujemy na obiekcie składającym się z kolekcji pobieranych za pomocą
``FetchType.LAZY`` oznacza to, że dla każdego gettera zostanie wykonane dodatkowe zapytanie do bazy danych celem
pobrania obiektów w kolekcji co obrazuje poniższy przykłąd.
```
List<String> getDescriptionsFromAllTasks(){
    return repository.findAll().stream()
    .flatMap(taskGropup-> taskGroup.getTasks().stream())
    .map(task -> task.getDescription())
    .collect(Collections().toList());
}
```
W powyższym przykładzie jako, że obiekty ``Task`` sa pobierane poprzez ``FetchType.LAZY`` osobne zapytanie zostanie 
wygenerowane każdorazowo dla ``getTasks()`` co może się objawić fatalną wydajnością.  
  
Problem ten można rozwiązać nadpisując metodę ``findAll()`` w taki sposób aby odrazu pobrała wszystkie obiekty ``Task``.  
Poniżej przykład wykorzystujący [język zapytań HQL](https://docs.jboss.org/hibernate/core/3.3/reference/en/html/queryhql.html) &#x2074;
```
@Override
    @Query("FROM TaskGroup g JOIN FETCH g.tasks")
    List<TaskGroup> findAll();
```

<br></br>
<br></br>
<br></br>

### Linki

&#x00B9; Dodatkowe ustawienia dla adnotacji @Transactional  
https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction-declarative-attransactional-settings  
&#x00B2; Strategie generowania kluczy głównych w Hibernate  
https://thorben-janssen.com/jpa-generate-primary-keys/  
&#x00B3; Kaskadowe operacje w Hibernate  
https://www.baeldung.com/jpa-cascade-types  
&#x2074; HQL- Hibernate Query Language
https://docs.jboss.org/hibernate/core/3.3/reference/en/html/queryhql.html  


