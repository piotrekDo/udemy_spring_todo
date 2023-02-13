# Security

## Keycloak

Keycloak to serwer uwierzytelniania i autoryzacji na licencji open-source. Może zostać podłączony
do [LDAP](https://sekurak.pl/podatnosc-ldap-injection-definicje-przyklady-ataku-metody-ochrony/),
np. [Active Directory](https://pasja-informatyki.pl/sieci-komputerowe/active-directory-wstep/) lub
uwierzytelniać użytkowników przy użyciu Google, Facebooka itd. Posiada również konsolę administracyjną, w której możemy
łatwo skonfigurować chociażby uprawnienia użytkowników.  
[Uruchamianie Keycloak](https://www.baeldung.com/spring-boot-keycloak)  
**UWAGA** Keycloak domyślnie uruchamia się na porcie 8080, możemy zmienić to zachowanie poprzez dodanie odpowiedniej flagi
``bin/kc.sh start-dev --http-port=8180``. Jeżeli wszystko się powiodło, pod adresem ``http://localhost:8180/`` powinniśmy 
zobaczyć stronę startową Keycloak  

## Keycloak - Realm

W wyświetlonym panelu tworzymy profil administratora i przechodzimy do logowania.  
Realm to osobne ustawienia dla aplikacji. Możemy utworzyć osobny Realm z własnymi ustawieniami. 