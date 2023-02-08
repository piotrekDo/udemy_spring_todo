# Thymeleaf

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

Thymeleaf jest technologią pozwalającą na tworzenie widoków dla aplikacji webowych napisanych w Java.  
[Dokumentacja i tutorial](https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.html)  
  
Wszystkie szablony muszą znajdować się w katalogu ``resources/templates``.  
Do wyświetlenia naszego dokumentu HTML potrzebujemy kontrolera, może on być oznaczony adnotacją ``@Controller``
ponieważ zwracamy tylko dokument HTML w postaci tekstu ``String``.
```
@Controller
@RequestMapping("/projects")
public class ProjectController {

    @GetMapping
    String showProjects(){
        return "projects";
    }
}
```
Spring mając za zadanie zwrócenia w metodzie ``String`` przeszuka najpierw pliki w katalogu ``resources/templates`` i zwróci 
dokument html odpowiadający wyrazowi zapisanemu w metodzie, w naszym przypadku ``resources/templates/projects.html``

## Atrybuty Thymeleaf
Do używania atrybutów, powinniśmy podać odowłanie do ich zbioru w znaczniku <html>  
``<html xmlns:th="http://www.thymeleaf.org">``  
Atrybuty służą do odwoływania się do kodu Java w naszym szablonie HTML. **Zapisujemy je od th:nazwa_atrybutu**  
Wewnątrz atrybutów posługujemy się [SpEL- Spring expression language](https://www.thymeleaf.org/doc/articles/standarddialect5minutes.html)  
W przykładzie poniżej zapisujemy atrybut ``action`` przekierowujący dane z formularza pod wskazany adres  
``<form method="post" th:action="@{/projects}">``  