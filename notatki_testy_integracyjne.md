# Testy integracyjne w Spring

Testy integracyjne określają, czy niezależnie opracowane jednostki oprogramowania działają poprawnie, gdy są ze sobą
połączone. Celem testów integracyjnych, jak sama nazwa wskazuje, jest sprawdzenie, czy wiele oddzielnie opracowanych
modułów współpracuje ze sobą zgodnie z oczekiwaniami.  
  
Testy integracyjne mogą polegać na testowaniu interakcji z bazą danych lub upewnianiu się, że mikrousługi współpracują
zgodnie z oczekiwaniami. Tego typu testy są droższe w prowadzeniu, ponieważ wymagają uruchomienia wielu części
aplikacji.

## Adnotacja @SpringBootTest

Używana w przypadku testów w Spring, sprawia że utworzony zostaje kontekst aplikacji. Używana najczęściej w testach
End2End.   
Do adnotacji ``@SpringBootTest`` możemy przekazać kilka parametrów

- [webEnvironment &#x00B9;](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/context/SpringBootTest.WebEnvironment.html)
  pozwala na określenie portu na którym zostanie uruchomiona aplikacja.
-

W przypadku webEnvironment.RANDOM_PORT możemy zapisać wylosowany port do zmiennej

```
@LocalServerPort
private int port;
```

## Adnotacja @AutoConfigureTestDatabase

W przyapadku testów integracyjnych musimy pamiętać o podmianie bazy danych, w przeciwnym razie możemy wprowadzić zmiany
do bazy produkcyjnej. Jednym z rozwiązań tego problemu jest zastosowanie adnotacji ``AutoConfigureTestDatabase`` nad
klasą
testową.

## TestRestTemplate

TestRestTemplate pozwala na symulowanie wysyłania zaoytań do naszego serwisu.

## Linki

&#x00B9; SpringBootTest.webEnvironment  
https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/context/SpringBootTest.WebEnvironment.html