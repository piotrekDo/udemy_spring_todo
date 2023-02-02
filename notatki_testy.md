# Testowanie w Spring

Testowanie oprogramowania to proces związany z wytwarzaniem oprogramowania. Zapewnia on jakość wytwarzanego
oprogramowania, a jego głównym celem jest sprawdzenie czy oprogramowanie jest zgodne z oczekiwaniami użytkownika (lub
np. specyfikacją projektu).

### Rodzaje testów

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