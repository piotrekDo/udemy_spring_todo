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
  
**Poniżej przykład z omówieniem**
w atrybucie ``th:obiect`` możemy zdefiować model obiektu, którym posługujemy się w formularzu. W wyrażeniu SpEL przekazujemy
referencję (nazwę) obiektu ``${project}``. Linijka powyżej stanowi odniesienie do projektu ``project`` do konkretnego modelu
w kodzie Java, w typ przypadku do ``ProjectWritemodel``.  
Następnie w ``input`` dzięki atrybutowi ``th:field`` możemy określić do jakiego pola obiektu odnosi się konkretny input.
Wykorzystując wyrażenie ``*{description}`` oznaczamy je jako pole ``object.description`` pochodzące z ``ProjectWriteModel``.  
  
W kontrolerze dzięki Springowemu wstrzykiwaniu zależności możemy do metody wstrzyknąć ``Model`` pochodzący z pakietu
``org.springframework.ui.Model``. Następnie wewnątrz metody tworzymy nowy obiekt ``ProjectWriteModel`` i ustalamy jego pole
``description``. Następnie poprzez metodę ``addAtribute()`` przypisujemy tak utworzony obiekt do referencji ``obiect`` 
wewnątrz szablonu. 
```    
<fieldset>
    <legend>Nowy projekt</legend>
    <!--/*@thymesVar id="project" type="com.example.demo.model.projection.ProjectWriteModel"*/-->
    <form method="post" th:action="@{/projects}" th:object="${project}">
        <label>Opis
            <input type="text" th:field="*{description}"/>
        </label>
    </form>
</fieldset>

@GetMapping
String showProjects(Model model){
    ProjectWriteModel projectWriteModel = new ProjectWriteModel();
    projectWriteModel.setDescription("test from controller");
    model.addAttribute("project" ,projectWriteModel);
    return "projects";
}
```

## Iterowanie- th:each

Atrybut ten pozwala na iterowanie po strukturze danch w podobny sposób to pętla ``forEach``
```
  <tr th:each="prod : ${prods}">
    <td th:text="${prod.name}">Onions</td>
    <td th:text="${prod.price}">2.41</td>
    <td th:text="${prod.inStock}? #{true} : #{false}">yes</td>
  </tr>
```
Zatem dla każdego ``prod`` wewnątrz struktury ``prods`` zostanie wygenerowana linia w tabeli.  
``th:each`` poza obiektem zwraca również kilka informacji dotyczących obiektu, na przykłąd indeks.
```
<fieldset th:each="step,stepStat: *{steps}">
    <legend th:text="|Krok ${stepStat.index + 1}|"></legend>
```
W przykładzie powyżej umieszczamy tekst składjący się z wyrazu ``Krok`` oraz dynamicznie generowanej wartośći pochodzącej
z indeksu. **Dodatkowo konkatencję możemy zapisać wewnątrz '| |', tzw. *pipe***  
Wewnątrz danyc zwracanych przez iterator mamy rónież dostęp do całkowitej liczby elementów, pierwszego, ostatnieogo czy
parzystych bądź nieparzystych elementów. 

### Zagnieżdżanie wyrażeń
Czasami potrzebujemy zagnieździć jedno wyrażenie wewnątrz innego, wykorzystujemy do tego zapis ``__wyrażenie__``  
Poniżej w znacznikach ``input`` musimy się odwołać do indeksu tablicy, dlatego wewnątrz nawiasów kwadratowych zagnieżdżamy
odowłanie do obecnego indeksu iteracji ``[__${stepStat.index}__]`` co zonacza ``[0], [1]`` itd.
```
<fieldset th:each="step,stepStat: *{steps}">
    <legend th:text="|Krok ${stepStat.index + 1}|"></legend>
    <label>Opis
        <input type="text" th:field="*{steps[__${stepStat.index}__].description}">
    </label>
    <label>Deadline
        <input type="date" th:field="*{steps[__${stepStat.index}__].daysToDeadline}">
    </label>
</fieldset>
```

## Walidacja

W ramach walidacji obiektu trafiającego z formularza do kontrolera możemy posłużyć się obiektem ``BindingResult``.
Odwołuje się on do **poprzedniego** argumentu metody. W poniższym przykładzie referencja ``bindingResult`` pozwala odwołać
się do ``current`` odpowiadającego właśnie obiektowi uzyskanemu z szablonu. 
```
@PostMapping
String addProject(@ModelAttribute("project") @Valid ProjectWriteModel current, BindingResult bindingResult, Model model ){
    if (!bindingResult.hasErrors()) {
        projectService.save(current);
        ProjectWriteModel projectWriteModel = new ProjectWriteModel();
        projectWriteModel.setDescription("test from controller");
        model.addAttribute("project", projectWriteModel);
        model.addAttribute("message", "Projekt został dodany");
    }
    return "projects";
}
```