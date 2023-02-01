# DTO i Serwisy

Serwisy stanowią rodzaj klas pomocniczych rozdzielających repozytoria od kontrollerów i zawierają logikę biznesową,
odpowiednią dla konkretnego rodzaju danych. Zajmują się między innymi przygotowaniem danych do wystawienia przez kontroller,
czy przygotowaniem danych zebranych prze kontroler do zapisu w bazie danych. Często również same są zależne od innych
klas pomocniczych mogących zajmować się walidacją czy przetwarzaniem danych między *encją* a *DTO*  
  
DTO- Data Transfer Object to rodzaj wzorca porojektowego powstały początkowo w celu ograniczenia ilości zapytań do baz
danych. Może zostać stworzony z kilku róznych obiektów, obecnie częściej służy do ukrywania danych, które są częścią
encji, a nie muszą być zwracane przez kontroler. Przykładowo model danych użytkownika będzie zawierał hasło lub dane
osobowe, które niekoniecznie chcemy zwracać w kontrolerze. DTO pozwala właśnie stworzyć taki wycinek danych potrzebnych
dla danej sytuacji. 