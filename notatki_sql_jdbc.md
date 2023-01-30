# SQL i Jdbc w Spring

### Flyway
Mechanizm migracji w bazie danych. Pozwala na przywrócenie stanu bazy danych w podobny sposób jak *GIT*. W Spring możemy
skorzystać z *Flyway* poprzez dodanie odpowiedniej zależności do pliku *pom.xml*.
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

Flyway do poprawnego działania potrzebuje dodatkowej konfiguracji w sytuacji gdy mamy już istniejącą bazę danych
````
  spring.flyway.baselineOnMigrate = true 
````

Powinniśmy także w folderze *resources* utworzyć dodatkowy pakiet- *db.migration*  
Wewnątrz niego możemy umieszczać kolejne pliki migracji. Każdy taki plik powinien zaczynać się od oznaczenia wersji
wg schematu ```V1__init_table.sql```  
Mamy więc plik .sql, gdzie V1__ oznacza wersję pierwszą. Dwie 'podłogi' oddzielają wersję od nazwy pliku.  


