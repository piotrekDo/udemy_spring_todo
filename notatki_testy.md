# Testowanie w Spring

Testowanie oprogramowania to proces związany z wytwarzaniem oprogramowania. Zapewnia on jakość wytwarzanego
oprogramowania, a jego głównym celem jest sprawdzenie czy oprogramowanie jest zgodne z oczekiwaniami użytkownika (lub
np. specyfikacją projektu).

## Rodzaje testów

Wyróżniamy cztery rodzaje testów:

- jednostkowe
- integracyjne
- systemowe
- akceptacyjne

Jako programiści mamy najwięcej styczności z testami jednostkowymi oraz integracyjnymi.  
**Testy jednostkowe** weryfikują działanie pojedyńczych części kodu, najczęściej metod, są szybkie i łatwe do
zautomatyzowania i zazwyczaj uruchamiane przy każdej zmianie kodu źródłowego.

**Testy integracyjne** weryfikują dzuałanie kilku modułów jednocześnie, sprawdzając działanie całej funkcjonalności,
wykonują się stosunkowo długo.

### Cechy dobrych testów- zasada FIRST

- **Szybkość (Fast)** - powinny wykonywać się szybko, aby nie wydłużać zbytnio czasu wykonania procesu budowania (tzw.
  buildu)
  oraz nie czekać zbyt długo na wyniki testów.
- **Niezależne (Isolated/Independent)** - testy powinny być od siebie niezależne (odizolowane). Uruchomienie jednego
  testu,
  nie powinno mieć wpływu na stan innych testów w tzw. test suite (oznaczającym pakiet testowy, np. testy w obrębie
  jednej
  klasy testującej).
- **Powtarzalne (Repeatable)** - powinny być powtarzalne na każdym środowisku. To znaczy, że nie powinny mieć zależności
  z
  innymi systemami np. bazą danych. Konfiguracja innych systemów nie powinna mieć wpływu na powtarzalność wykonywania
  testów na różnych środowiskach. Jeśli test zostanie uruchomiony np. 10 razy, to tyle samo razy powinniśmy otrzymać ten
  sam wynik. Wykorzystywanie klasy Random, poleganie na kolejności elementów w kolekcjach nieuporządkowanych, czy
  korzystanie z rzeczywistych timestampów (poprzez wykorzystanie np. LocalDateTime.now()) może powodować problemy ze
  stałością wyników testów.
- **Samosprawdzające (Self-checking)** - stwierdzające czy test przeszedł lub nie (brak ręcznej interpretacji).
- **Kompletne (Thorough)** — pisane razem z testem produkcyjnym. Powinniśmy sprawdzać przypadki testowe podając wartości
  dające wyniki pozytywne, negatywne oraz korzystając z wartości granicznych. Dzięki czemu testy będą dokładniej
  sprawdzać
  napisany kod.

**Pozostałe dobre praktyki**

- **Pojedyńcza odpowiedzialność** - jeden test sprawdza jedną funkcjonalność.
- ***Second Class Citizens*** - kod testowy nie jest kodem drugiej kategorii. Należy o niego dbać tak samo jak o kod
  produkcyjny.

## Zależnośći, jak zacząć?

Do wykonywania testów niesbędna będzie biblioteka *JUnit*, przyda się rónież *Mockito* czy *AsertiJ*. Wszystkie te biblioteki
są zawarte w zależnośći ``spring-boot-test``
```
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
  </dependency>
```
  
Test powinien posiadać adnotację ```@Test```, jest metodą, która nic nie zwraca (wykorzystujemy słowo kluczowe ``void``).
Test o ile nie jest *testem parametryzowanym* nie posiada argumentów wejściowych.  
Dobry test składa się z trzech sekcji:
- ``given`` definiujemy warunki początkowe testu, przygotowujemy wszystkie obiekty potrzebne do przeprowadzenia testu.
- ``when`` najczęściej jednolinijkowa, w której wywołujemy testowaną metodę.
- ``then`` wykorzystujemy tzw. *asercje*, sprawdzające oczekiwany resultat z rzeczywistym.

## Asercje

Asercje są instrukcjami, których celem jest potwierdzenie, że uzuskane obiekty w teście spełniają pewne założenia, czyli
ostatecznie spraawdzają poprawoność wykonania testu. Asercje są statycznymi metodami pochodzącymi z klasy 
```org.junit.api.Assertions```, które pozwalają na porówanie wyników działania określonej funkcji z oczekiwaną wartością.  
  
### Asercje podstawowe

*JUnit* oferuje wiele asercji, każda z nich ma wiele przeciążeń, a najczęściej wykorzystywane to:

- ``assertEquals(oczekiwany, rzeczywisty)`` - porównuje obiekty za pomocą metody ``equals``. Oczekiwana wartość zawsze
powinna być pierwszym argumentem.
- ``assertNotNull(obiekt)`` - sprawdza, czy dany obiekt **nie jest** równy ``null``.
- ``assertNull(obiekt)`` - sprawdza, czy dany obiekt **jest** równy ``null``.
- ``assertTrue(wartość_logiczna)`` - oczekuje, że przekazana wartość ``boolean`` jest równa ``true``.
- ``assertFalse(wartość_logiczna)`` - oczekuje, że przekazana wartość ``boolean`` jest równa ``false``.
- ``assertSame(obiekt1, obiekt2)`` - oczekuje, że **referencje** są takie same, porównując obiekty za pomocą ``==``.
- ``assertNotSame(obiekt1, obiekt2)``- sprawdza czy **referencje** są różne.
- ``assertFail()`` - natychmiast kończy test niepowodzeniem. Wykorzystywana do testowania wyjątków metodą ``try catch``.

### Asercje zaawansowane- JUnit 5

JUnit w wersji 5 wprowadza również szereg dodatkowych asercji, np:

- ``assertArrayEquals(oczekiwana_tablica, faktyczna_tablica)`` - sprawdza równość oraz kolejność wszystkich elementów 
w obydwu tablicach.
- ``assertIterableEquals(oczekiwana_struktura_danych, faktyczna_struktura_danych)`` - sprawdza róność i kolejność wszystkich
elementów w obiektach implementujących interfejs ``Iterable``.
- ``assertLinesMatch(oczekiwana, faktyczna)`` - sprawdza z pomocą pewnego algorytmu i wyrażeń regularnych czy dwie listy
String zawierają takie same elementy.
- ``assertTimeout(czas, executable)`` - sprawdza, czy kod wykonuje się szybciej niż przekazana wartość ``timeout``.
  
Każdą z asercji możemy przeciążyć, dodając kolejny parametr z wiadomością wyświetlaną w przypadku, gdy taka asercja 
zakończy się niepowodzeniem.  

### Asercje grupujące

W sejcji ``then`` możemy wykonać dowolną liczbę asercji. W przypadku, gdy takich asercji bęzie kilka a jedna z nich zakończy
się niepowodzeniem, kolejne **nie zostaną wykonane**. Jeżeli chcemy aby w teście zawsze wykonały się wszystkie asercje,
musimy je wywołać wewnątrz asercji ``assertAll(...)``. Asercja ta wykorzystuje ``varargs``, oczekując obiektu typu 
``Executable``. Obiekt taki możemy zaimplementować za pomocą bezargumentowej lambdy:
```
assertAll(
  () -> assertEquals(expectedObject, actualObject, "Assertion 1 failed"),
  () -> assertTrue(actualResult, "Assertion 2 failed")
);
```

## Adnotacje

### Metody cyklu życia testów

JUnit pozwala na pisanie testów, za pomocą dodania adnotacji ``@Test`` do metody. Tworząc wiele testów dla jednej klasy,
możliwe jest, że nasz kod będzie zawierał jakieś powtórzenia.  
  
Celem uniknięcia duplikacji kodu JUnit pozwala zdefiniować tzw. ``metody cyklu życia testu``. Metody te, nad swoją
sygnaturą muszą posiadać jedną z adnotacji:
- ``@BeforeEach``
- ``@AfterEach``
- ``@BeforeAll``
- ``@AfterAll``

Należy pamiętać, że wszystkie metody cyklu życia testu:
- nie zwracają żadnego obiektu,
- mogą mieć dowolną nazwę,
- nie przyjmują argumentów, 
- nie muszą posiadać modyfikatora dostępu,
- są opcjonalne i możemy je definiować wielokrotnie- jedna klasa może nie posiadać żadnej metody z adnotacją ``@AfterEach``
a może mieć kilka z adnotacją ``@BeforeEach``.
- mogą znaleźć się w dowolnym miejscu w klasie testowej, w praktyce jednak najczęściej są definiowane przed testami.
  
``@BeforeEach`` - fragment kod wywoływany **przed każdą** metodą testową. Celem tej metody jest przygotowanie obiektów
dla każdego z testów.  
``@AfterEach`` - metoda wywoływana **po każdej** metodzie testowej. Służy do usuwania danych lub przywracania stanu
początkowego po każdym teście.  
``@BeforeAll`` - **musi być metodą statyczną**. Jest wywoływana **raz, na początku wykonywania testów** w danej klasie.
Służy głównie do ustanawiania wartości obiektów, które będą wykorzystywane w testach, ale nie będą modyfikowane, lub 
takich, które są kosztowe jak np. nawiązanie połączenia z bazą danych.  
``@AfterAll`` - **musi być metodą statyczną**. Zostanie ona wywołana **raz, po wykonaniu testów** w całej klasie. 
Przydatna do usuwania obiektów, które były wspólne dla wszystkich testów, jak np. zamykanie połączenia z bazą danych.  

**UWAGA** *w JUnit 4, anotacje te mają nieco inne nazwy i są to odpowiednio: ```@Before```, ```@After```, 
```@AfterClass```, ```@BeforeClass```.*

### Adnotacje dodatkowe

Biblioteka JUnit dostarcza również inne, użyteczne adnotacje jak np. 
- ``@DisplayName`` - pozwala na nadanie własnej nazwy dla metod i klas testówych. Naz te będą wyświetlane podczas uruchamiania
testów i wyświetlania wyników.
- ``@Disabled`` - oznacza metodę lub klasę, która nie powinna być uruchamiana w trakcie testów. Przyjmuje ona argument,
w którym możemy podać przyczynę wyłączenia testu.  





















