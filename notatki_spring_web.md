# Spring Web

Moduł Spring umożliwiający tworzenie aplikacji webowych. Zawiera zależoności do  kontenera serwletów Tomcat.

## Adnotacja @RequestMapping

Adnotacja ``@RequestMapping`` służy do określania adresu *endpintu* można część wspólną wynieść na poziom klasy. W taki 
sposób wszystkie metody klasy będą miały wspólną część adresu co upraszcza kod. W ten sposób obydwie medody będa 
dostępne pod adresem ``/tasks``

```
@RestController()
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET, params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all the tasks");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping()
    ResponseEntity<Page<Task>> readAllTasks(Pageable page) {
        logger.warn("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page));
    }
```

Warto zwrócić uwagę na użyteczny parametr określany przez wszystkie adnotacje pochodzące od *mapping*- ``produces=``  
```
@GetMapping(value="/search/done", produces = MediaType.APPLICATION_JSON_VALUE)
String foo(){return "";}

@GetMapping(value="/search/done", produces = MediaType.TEXT_XML_VALUE)
String bar(){return "";}
```

w powyższym przykładnie w zależności od nagłówka **ACCEPT** zostanie przekierowany pod konkretny endpoint.

## @RequestParam

Adnotacja stosowana przy określaniu paramterów metody. Określa czy parametr taki ma zostać powiązany z parametrami
żądania HTTP. Dzięki temu można się do nich odnieść w kodzie aplikacji zamiast żądać przekazania obiektu.

```
    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true", required = false) boolean state) {
        return ResponseEntity.ok(
                repository.findAllByDone(state)
        );
    }
```

jako argumenty adnotacji możemy między innymi przekazać parametr ``defaultValue`` oraz ``required``. W powyższym przykładzie
``required`` nie jest wymagane jako że przekazujemy wartość domyślną. Jeżeli nie ustawimy wartości domyślnej i nie zmienimy
parametru ``required`` na false to nieprzekazanie parametru w żądaniu zakończy sięwystąpieniem wyjątku. 

## Użyteczne paramtry metod

``Principal`` - pozwala na uzyskanie dostępu do danych zalogowanego użytkownika
```
@GetMapping
void lol(Principal p) {
    System.out.printline(p.getName())
}
```

``HttpServletRequest`` oraz ``HttpServletResponse`` - pozwalają uzyskać dostęp do całości żądania i odpowiedzi.

[lista praametrów z dokumentacji](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments)

# Filter

Filtry to fragmenty kodu wykonywane przed przetworzeniem każdego żądania. Mogą modyfikować, żądanie, sprawdzać je czy 
odrzucać. Zaletą filtrów może być odrzucanie niechcianch połączeń jeszcze przed ich przetworzeniem przez aplikację, co
pozwala na oszczędność zasobów. Jednak obecność filtra należy traktować jak osobną, dodatkową warstwę, co może powodować
wydłużenie czasu odpowiedzi na żądanie użytkownika.  
  
## Tworzenie własnego Filtra

Tworzymy klasę implementują interfejs ``Filter`` pochodzący z pakietu ``jakarta.servlet``. Musimy zaimplementować teraz
metodę ``doFilter``. Następnie oznaczamy klasę adnotacją ``@Component`` aby mogła zostać wykorzystana przez Spring'a.   
**WAŻNE!** jeżeli chcemy przepuścić żądanie dalej mumimy wywołać metodę ``chain.doFilter(request, response);`` w przeciwnym
razie żądanie zostanie zatrzymane w tym miejscu. 

## Kolejność wykonywania filtrów i adnotacja @Order

W ramach naszej aplikacji możemy definiować wiele filtrów. Dzięki implementacji interfejsu ``Ordered`` a następnie 
nadpisaniu metody ``getOrder`` możemy zdefiniować priorytet naszego filtra zwracając wartość liczbową w metodzie.  
**Priorytet działa na zasadzie kolejnośći. Filtr z ``order`` 1 wykona się przed filtrem z ``order`` 2.**  
  
Zamiast implementacji interfejsu możemy wykorzystać adnotację ``@Order`` i jako parametr przekazać wartość liczbową.

