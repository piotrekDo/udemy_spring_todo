# DTO i Serwisy

**Serwisy** stanowią rodzaj klas pomocniczych rozdzielających repozytoria od kontrollerów i zawierają logikę biznesową,
odpowiednią dla konkretnego rodzaju danych. Zajmują się między innymi przygotowaniem danych do wystawienia przez
kontroller, czy przygotowaniem danych zebranych prze kontroler do zapisu w bazie danych. Często również same są zależne
od innych klas pomocniczych mogących zajmować się walidacją czy przetwarzaniem danych między *encją* a *DTO*.

**DTO- *Data Transfer Object*** to rodzaj wzorca porojektowego powstały początkowo w celu ograniczenia ilości zapytań do
baz
danych. Obiekt DTO Może zostać stworzony z kilku róznych obiektów, obecnie częściej służy do ukrywania danych, które są
częścią encji, a nie muszą być zwracane przez kontroler. Przykładowo model danych użytkownika będzie zawierał hasło lub
dane osobowe, które niekoniecznie chcemy zwracać w kontrolerze. DTO pozwala właśnie stworzyć taki wycinek danych
potrzebnych dla danej sytuacji.

## Scope

Scope jest zakresem w jakim *Bean* może istnieć. Domyślnie jest to **singleton** co oznacza, że istnieje dokładnie jeden
obiekt danej klasy i jest on wstrzykiwany do innych *beanów*. Do określenia takiego zakresu wykorzystujemy
adnotację ``@Scope`` umieszczaną pod adnotacją ``@Component`` lub pokrewną. Wykorzystujemy do tego parametr w postaci
enum ``ConfigurableBeanFactory``.  
Poza ``.SCOPE_SINGLETON`` możemy skorzystać z:
- ``.SCOPE_PROTOTYPE`` - tworzy nowy obiekt za każdym razem gdy klasa jest wykorzystywana. Tworzona jest nowa instancja.
- ``.SCOPE_REQUEST`` - tworzy obiekt tylko na czas żądania, konieczne jest jeszcze skonfigurowanie parametru ``proxyMode`` na ``TARGET_CLASS.
    Zamiast tego można wykorzystać adnotację ``@RequestScope``.
- ``SCOPE.SESSION`` - tworzny obiekt istnieje na czas sesji użytkownika.