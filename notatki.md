# Notatki

mvnw - maven wrapper pozwala na uruchomienie projektu tworzonego w Maven na komputerach nie mających 
zainstalowanego Mavena

Convention over configuration - korzystamy z domyślnych konfiguracji zamiast tworzyć włane, usprawnia i przyśpiesz 
tworzenie oprogramowania.

Adnotacje JPA:
Entity- tworzy tabelę odpowiadjącej klasie.
Table(name = "some_name") pozwala wprowadzić nazwę tabeli, można to zrobić bezpośrednio w @Entity.
@Id  określa klucz głowny tabeli, adnotacja wymagana.
    @GeneratedValue określa strategię generowania klucza głównego
@Column - adnotacja umieszczana nad konkretnymi polami definiuje kolumne. Można ją tez umieszczać na getterach
    name = "some_name" pozwala ustalić nazwę kolumny



Repozytorium - tworzymu interfejs rozszerzający JPARepository, JPARepository jest generyczne i przyjmuje 2 wartości:
    Klasę oraz klucz główny, kolejność jest bardzo ważna, należy uważać aby nie zapisać tego odwrotnie.

@RepositoryRestResource - adnotacja umieszczana w repozytorium utworzy również kontroller(??). Pochodzi z
    spring-boot-starter-data-rest
@RestResource - adnotacja umieszczana przy nadpisywaniu metod z repozytorium, zmieniając wewnątrz niej atrubut
    exported na false sprawimy, że metoda nie będzie dostępna dla użytkowników. Adnotacja przyjmuje też kilka innych
    atrybutów jak path określających nazwę ścieżki pod którą zasób jest dostępny czy rel określającą nazwę samego zasobu.


Jackson: mappper wbudowany w Springa pozwalający przetworzyć obiekt do postaci JSON, wymaga publicznego dostępu.



Walidacja: można to zrobić na kilka sposobow, np w setter. Są też od tego adnotacje. 
@NotBlank() - nie może być nullem, pustym stringiem, nie może zawierać samych spacji. W ramach atrybutu message możemy
    przekazać treść wiadomości błędu. 

Mappowanie błędów (ExceptionHandler?) walidacyjnych możemy osiągnąc implementując interface RepositoryRestConfigurer w klasie posiadającej 
    metodę main. Następnie nadpisujemy metodę configureValidatingRepositoryEventListener. Tutaj możemy dodać validatory
    zwracane z metody tworzącej LocalValidatorFactoryBean


Testy w Postman: Zakładka tests oferuje kilkanaście predefiniowanych testów jakie możemy wykonać na naszym kontrollerze. 

SpringCloud: mikroserwisy to styl tworzenia aplikacji skladajacej sie z niezaleznych aplikacji, kazda moze byc stworzona 
    w roznych jezykach. Rozwiązanie wypromowane i rozbudowane przez serwis Netflix. Spring Cloud w dużej mierze składa się
    z bibliotek, rozwiązań opracowanych przez Netflix. 

Spring Batch: moduł pozwalający przetwarzać większe parie danych pozyskane z bazy. 

HATEOAS: Hypermedia as the Engine of Application State. Stan aplikacji reprezentowany jako Hyperdata. Stanowi rozszerzene
    dla REST. 

@RepositoryRestController: umieszczamy nad osobną klasą będącą kontrolerem właściwym dla repozytorium. Do prawidłowego 
    działania wymaga dostarczenia obiektu repozytorium. Można to zrobić poprzez wstrzykiwanie zależności. 
    Możemy tutaj nadpisywać metody dostępne w ramach repozytorium i uzupełniać je o dodatkową logikę. Zapisując wszystko
    w takiej postaci otrzymamy nasz zasób bez metadanych dostarczanych w ramach HATEOAS. Aby je ptrzymać musimy doddac
    do naszego projetku zależność do SpingHATEOAS, wówczas uzyskamy dostęp do obiektu Resources. Do obiektu Resources 
    dodajemy dane pozyskane z bazy danych a sam obiekt Resources zwracamy jako zawartość ResponseEntity. Istnieje również
    obiekt ResourceProcessor.

Metoda zawarta w takim kontrolerze powinna posiadać adnotację @RequestMapping, gdzie wskazujemy jaką metodę z repozytorium
    chcemy nadpisać, tak aby zamiast niej wywołała się nasza metoda. Możemy również użyc odpowiedniego dla metody mappingu
    jak @GetMapping czy @PostMapping itd.
    Do metody @RequestMapping możemy również przekazać atrybut params, gdzie możemy na przykład wskazać w jakich 
    przypadkach metoda NIEpowinna zostać uruchomiona. Jeżeli chcemy stronicować w ramach metody w ramach kontrolera
    musimy utworzyć przeciążenie metody. 

ResponseEntity to obiekt wrappujący na obiekty zwracane przez kontrolery. Pozwala to na przykład na ustalanie kodu 
    odpowiedzi, gdzie domyślnie jest to kod 200, możemy ustalić kod 201. 







