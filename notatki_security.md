# Security

## Keycloak

Keycloak to serwer uwierzytelniania i autoryzacji na licencji open-source. Może zostać podłączony
do [LDAP](https://sekurak.pl/podatnosc-ldap-injection-definicje-przyklady-ataku-metody-ochrony/),
np. [Active Directory](https://pasja-informatyki.pl/sieci-komputerowe/active-directory-wstep/) lub
uwierzytelniać użytkowników przy użyciu Google, Facebooka itd. Posiada również konsolę administracyjną, w której możemy
łatwo skonfigurować chociażby uprawnienia użytkowników.  
[Uruchamianie Keycloak](https://www.baeldung.com/spring-boot-keycloak)  
**UWAGA** Keycloak domyślnie uruchamia się na porcie 8080, możemy zmienić to zachowanie poprzez dodanie odpowiedniej flagi
``bin/kc.bat start-dev --http-port=8180``. Jeżeli wszystko się powiodło, pod adresem ``http://localhost:8180/`` powinniśmy 
zobaczyć stronę startową Keycloak  

## Keycloak - Realm

W wyświetlonym panelu tworzymy profil administratora i przechodzimy do logowania.  
Realm to osobne ustawienia dla aplikacji. Możemy utworzyć osobny Realm z własnymi ustawieniami. 

### Client

Konfigurujemy nowego klienta a więc aplikację łączącą się z naszym Realm. Tutaj ważnym ustawieniem jest
``Valid redirect URIs`` czyli adres na który Keycloak ma przekierować użytkownika po poprawnym zalgoowaniu. Na przykład 
``localhost:8080/*`` gwiazdka na końcu pozwoli odowłać się do różnych zasobów wewnątrz aplikacji. 

### Role, użytkownicy

W ramach ustawień *Realm roles* możemy zdefiniować różne role użytkowników. W ramach aplikacji możemy zezwolić użytkonikom
na rejestrację lub wprowadzać nowych ręcznie. 

## Integracja ze Springiem

Do rozpoczęcia współpracy naszej aplikacji z Keycloak potrzebujemy kilku zależnośći:
Spring Boot Security- dla uruchomienia security.
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

Client OAuth2 dla współpracy z zewnętrznymi aplikacjiami.
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

OAuth2 resource server dla oddelegowania walidacji JWT do Keycloak.
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

Adapter dla Keycloak
```
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-spring-security-adapter</artifactId>
    <version>20.0.3</version>
</dependency>
```
  
Następnie w ramach pliku application.properties / yml dopisujemy kilka ustawień
```
keycloak:
  auth-server-url: 'http://localhost:8180/auth'
  realm: 'TodoApp'
  resource: 'todo-app-client-spring'
  public-client: true
```
Określamy adres Keycloak, 'królestwo', *resource* oznacza nas jako aplikację i musi pokrywać się z *client-id* zdefiniowanym
po stronie Keycloak. Ustawiamy również flagę *public-client* na ``true``.  
  
  
Klasa konfiguracyjna
```
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
public class SecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

    @Bean
    KeycloakSpringBootConfigResolver keycloakSpringBootConfigResolver(){
        return new KeycloakSpringBootConfigResolver();
    }


    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Override
    public void init(WebSecurity builder) throws Exception {

    }

    @Override
    public void configure(WebSecurity builder) throws Exception {

    }
}
```

## Wylogowanie w Keycloak

Mając skonfigurowane ustawienia Keycloak wystarczy nam kontroler z metodą wywołującą ``request.logout()``. Następnie zwracamy
np. stronę startową aplikacji w celu przekierowania.
```
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class SsoController {
    @GetMapping("/logout")
    String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "index";
    }
}
```

## Security w Thymeleaf

[Security w Thymeleaf](https://www.baeldung.com/spring-security-thymeleafhttps://www.baeldung.com/spring-security-thymeleaf)
na początek dodajemy zależnośći
```
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity5</artifactId>
</dependency>
```
I dopisujemy w HTML ``<html lang="en" xmlns:sec="http://www.thymeleaf.org">``.


## Zabezpieczenia endpointów z poziomu kontrolera

Możemy do metody wstrzyknąć obiekt ``Authentication`` pochodzący z pakietu ``import org.springframework.security.core.Authentication;``
a następnie wewnątrz metody sprawdzić role. 
```
@GetMapping
String showProjects(Model model, Authentication auth) {
    if (auth.getAuthorities().stream().anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"))) {
        ProjectWriteModel projectWriteModel = new ProjectWriteModel();
        model.addAttribute("project", projectWriteModel);
        return "projects";
    }
    return "index";
    }
```
Wadą takiego rozwiązania jest ``NullPointerException`` w przypadku, gdy użytkownik nie jest zalogowany a usiłuje dostać się
do zasobu. Nie ma wówczas utworzonego obiektu Athentication. Można się zabezpieczyć *null checkiem*  

### Zabezpieczenia adnotacjami- aspekty

Rozpoczynamy od anotowania klasy konfiguracyjnej
```
@EnableGlobalMethodSecurity(
        jsr250Enabled = true,
        securedEnabled = true,
        prePostEnabled = true
)
@KeycloakConfiguration
public class SecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {
```

#### @Secured

Można zastosować nad klasą lub metodą
```
    @Secured("ROLE_ADMIN")
    @GetMapping("/url")
    String url() {
        return dataSourceProperties.getUrl();
    }
```

Alternatywnie można wykorzystać adnotację ``@RolesAllowed``
```
    @RolesAllowed({"ROLES_ADMIN"})
    @GetMapping("/prop")
    boolean myProp() {
        return taskConfigurationProperties.getTemplate().isAllowMultipleTasks();
    }
```

Obydwie adnotacje przyjmują tablicę String, reprezentującą dopuszczalne role użytkownika. 
