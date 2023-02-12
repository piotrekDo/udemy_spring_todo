# Zdarzenia  

Spring pozwala reagować na różne zdarzenia w ramach działania naszej aplikacji. Aplikacja może regować na wystąpienie jakiegoś
*zdarzenia*, takiego jak start programu czy wyołanie jakiejś funkcji. 
Aby utowrzyć nowe zdarzenie tworzymy klasę oznaczoną jako ``@Component`` i rozszerzamy interfejs ``ApplicationListener``.
Interfejs ten jest generyczny i musimy przekazać obiekt rozszerzający ``ApplicationEvent``.  
*ApplicationListener* to interejs dedykowany specjalnie do obsługi klas zarządzających zdarzeniami. Jest on generyczny i 
musimy przekazać do niego coś rozszerzającego *ApplicationEvent*, jest to specjalna lista klas określających konkretne zdarzenia.
Klas tych jest wiele i najlepiej jest je podejżeć w hierarchii klas ``ctrl + H``.
  
Poniżej zdefiniowaliśmy zdarzenie reagujące na utworzenie kontekstu Spring.
```
@Component
public class Warmup implements ApplicationListener<ContextRefreshedEvent> {
    private final static Logger logger = LoggerFactory.getLogger(Warmup.class);
    private final TaskGroupRepository taskGroupRepository;

    public Warmup(TaskGroupRepository taskGroupRepository) {
        this.taskGroupRepository = taskGroupRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Application warmup after context refreshed");
        String description = "ApplicationContextEvent";
        if (!taskGroupRepository.existsByDescription(description)){
            logger.warn("No required group found, adding it now");
            TaskGroup taskGroup = new TaskGroup();
            taskGroup.setDescription(description);
            taskGroup.setTasks(Set.of(
                    new Task("ContextClosedEvent", null, taskGroup),
                    new Task("ContextRefreshedEvent", null, taskGroup),
                    new Task("ContextStoppedEvent", null, taskGroup),
                    new Task("ContextStartedEvent", null, taskGroup)
            ));
            taskGroupRepository.save(taskGroup);
        }
    }
}
```

## ApplicationEventPublisher

Klasa pozwalająca obublikować zdarzenie.  
  
Dobrą praktyką jest publikacja zdarzeń w ramach metody oznaczonej adnotacją ``@Transactional``.

## Własne zdarzenia

