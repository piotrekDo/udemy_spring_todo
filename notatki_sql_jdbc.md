# SQL i Jdbc w Spring

## Flyway
Mechanizm migracji w bazie danych. Pozwala na przywrócenie stanu bazy danych w podobny sposób jak *GIT*. W Spring możemy
skorzystać z *Flyway* poprzez dodanie odpowiedniej zależności do pliku *pom.xml*. Flayway pozwala na zapis kodu SQL
w skryptach sprawdzanych przez aplikację każdorazowo gdy jest uruchamiana. Flayway kontroluje które zmiany (nazywane
*migracjami*) zostały już zastosowane. W sytuacji, gdzie pojawia się nowy skrypt jest on jednorazowo wykonywany na 
bazie danych. Pozwala to również na łatwe wycofanie zmian o ile nie usuneliśmy danych. 
```
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
```

warto również ustawić własność DDL na *validate* dzięki czemu Hibernate będzie weryfikował zgodność modelu danych z aplikacji
ze stanem bazy danych.
```
spring.jpa.hibernate.ddl-auto: validate
```

Flyway do poprawnego działania potrzebuje dodatkowej konfiguracji w sytuacji gdy mamy już istniejącą bazę danych.  
Powinniśmy folderze *resources* utworzyć dodatkowe foldery- ***db/migration***    
Wewnątrz folderu *migration* możemy umieszczać kolejne pliki migracji. Każdy taki plik powinien zaczynać się od oznaczenia wersji
wg schematu ```WERSJA__NAZWA_SKRYPTU.sql```, np. *V1__init_table.sql*. Wersja powinna być rozdzielona dwoma znakami 
'*podłogi*', jest to konwencja *Flyway.*
<br></br>

#### *Zmiana w plikach migracji*
Flyway dla każdego wykonanego pliku migracji wylicza tzw. *checksum* - sumę kontrolną wskazującą na rozmiar pliku.
Dane te są zapisywane w bazie danych w specjalnej tabeli tworzonej na użytek *Flyway* - *'flyway_schema_history'*.
**Flyway nie dopuszcza edycji wykonanych plików migracji**. Jeżeli przy starcie aplikacji *Flyway* wykryje zmianę w wykonanym
pliku program się zatrzyma. Chcąc dokonać zmiany na instniejącej bazie **należy stworzyć kolejny plik migracji.**


### Migracja Java z pomocą Spring
Możliwe jest wykonanie migracji [z pomocą Spring](https://flywaydb.org/documentation/concepts/migrations#spring) &#x00B9;.
Możemy to osiągnąć tworząć w pakiecie *java/db/migration*, **nie w folderze *resources/db/migration*** klasy rozszerzające
klasę *BaseJavaMigration*. Nazwa tak utworzonej klasy musi podążać za wcześniej wspomnianą konwencją *Flyway*- WERSJA__NAZWA_KLASY
Klasa taka *powinna też być publiczna*, musimy w niej zaimplementować metodę *migrate*.  
  
Przykład:  
```
package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class V2__insert_example_todo extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .execute("INSERT INTO tasks (description, done) VALUES ('Learn Java migrations', true)");
    }
}
```




<br></br>
<br></br>
<br></br>
### Linki
&#x00B9; Migracje w Spring  
https://flywaydb.org/documentation/concepts/migrations#springhttps://flywaydb.org/documentation/concepts/migrations#spring