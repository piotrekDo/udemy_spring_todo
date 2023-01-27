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

RepositoryRestResource - adnotacja umieszczana w repozytorium utworzy również kontroller(??). Pochodzi z
    spring-boot-starter-data-rest

Jackson: mappper wbudowany w Springa pozwalający przetworzyć obiekt do postaci JSON, wymaga publicznego dostępu.



Walidacja: można to zrobić na kilka sposobow, np w setter. Są też od tego adnotacje. 
@NotBlank() - nie może być nullem, pustym stringiem, nie może zawierać samych spacji. W ramach atrybutu message możemy
    przekazać treść wiadomości błędu. 

Mappowanie błędów (ExceptionHandler?) walidacyjnych możemy osiągnąc implementując interface RepositoryRestConfigurer w klasie posiadającej 
    metodę main. Następnie nadpisujemy metodę configureValidatingRepositoryEventListener. Tutaj możemy dodać validatory
    zwracane z metody tworzącej LocalValidatorFactoryBean