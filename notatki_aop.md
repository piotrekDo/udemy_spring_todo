# Aspect oriented programming

Programowanie zorientowane aspektowo to podejście, które ummożliwia przechwycenie działań danej metody w celu
uruchomienia
jakiegoś fragmentu kodu przed lub po logicy wykonywanej w przechwytywanej metodzie. Bazuje na niejawnym wykonywaniu
metod.

Zastosowanie:

- bezpieczeństo, np. sprawdzenie uprawnień uzytkownika chcącego uzyskać dostęp do jakiegoś zasobu.
- logowanie zdarzeń.
- obsługa wyjątków.

Spring wykorzystuje *AOP* np. do obsługi transkacji lub ``@ExceptionHandler```.

**Podstawowe pojęcia dotyczące AOP**  
**punkt przeciącia- pointcut** - jest to wyrażenie (zazwyczaj w postaci ścieżki lub adnotacji), które definiuje miejsce
wywołania porady *advice*.  
**Porada- advice** - jest metodą, która zostanie wywołana w momencie gdy warunek zdefiniowany w *punkcie przecięcia*
zostanie
spełniony. Dostępnych jest kilka rodzajów porad w zależności od tego, co chcemy osiągnąć:  
``@Before`` - ten rodzaj porady wykona się **przed** metodą biznesową.
``@After`` - porada wykonująca się **po** wskazanej metodzie biznesowej.
``@Around`` ten rodzaj porady wykona się przed lub **zamiast** metody.

Domyślnie Spring wykorzystuje bibliotekę ``AspectJ``. Aspect to klasa zawierająca porady, oznaczona
adnotacją ``@Aspect``,
musi być *komponentem Spring*.

## Tworzenie własnej adnotacji

W celu zdefiniowania własnej adnotacji tworzmy klasę implementującą interface oznaczony @.  
``@interface MyAnnotation {}``  

Nowo utworzoną adnotację powinniśmy skonfigurować **dodatkowymi adnotacjami**.  
``@Retention`` określa jak długo adnotacja ma obowiązywać. W przypadku gdy nie oznaczymy naszej adnotacji
adnotacją ``@Retention``
zostanie ona utworzona z domyślną wartością:  
``RetentionPolicy.CLASS`` -adnotacja nie będzie obowiązywała w czasie działania programu, *przydatne podczas
przetwarzania końcowego na poziomie kodu bajtowego* ***WUT?***  
``RetentionPolicy.SOURCE`` - odrzucane w trakcie kompilacji, adnotacje te pełnią rolę informacyjną jak ``@Override``.  
``RetentionPolicy.RUNTIME`` - adnotacje używane w trakcue działania programu pełniące jakies role.   
  
Drugą adnotacją jakiej powinniśmy użyć przy tworzeniu własnej to ``@Target``.  
Określa ona gdzie możemy tej adnotacji użyć, np. na metodzie czy klasie. Służy do tego parametr ``ElementType``. Jest to 
enum zawierający wiele opcji, najczęściej jest to:  
``ElementType.Type`` - klasa, interfejs, enum.
``ElementType.Field`` - pola klasy czy enuma.
``ElementType.Method`` - deklaracja metody.
``ElementType.Parameter`` - parametr metody jak np. ``@Valid``.

## Adnotacja @Timed

Adnotacja pochodząca z pakietu ``io.micrometer.core.annotation`` pozwalająca mierzyć czas działania metody

## Tworzenie aspektu

Tworzymy więc klasę oznaczoną adnotacjami ``@Aspect`` oraz ``@Component``. Wewnątrz takiej klasy definiujemy *porady*.

```
@Aspect
@Component
public class LogicAspect {
    private final Timer projectCreateGroupTimer;

    public LogicAspect(final MeterRegistry registry) {
        this.projectCreateGroupTimer = registry.timer("logic.project.create.group");
    }

    @Around("execution(* com.example.demo.logic.ProjectService.createGroup(..))")
    Object aroundProjectCreateGroup(ProceedingJoinPoint jp) {
        return projectCreateGroupTimer.record(() -> {
            try {
                return jp.proceed();
            } catch (Throwable e) {
                if (e instanceof RuntimeException) throw (RuntimeException) e;
                else throw new RuntimeException(e);
            }
        });
    }
}
```

