# Przykłady testów jednostkowych

## Przykład A

Przykład ten pokazuje wykorzystanie adnotacji ``@Test``. Zwróć również uwagę na podział testów na sekcje ``given``, ``when`` i ``then``,
a także fakt, że klasa testowa i same testy nie są publiczne.

```
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NameVerifierTest {

  @Test
  void shouldValidateName() {
    String name = "Andrzej";
    NameVerifier nameVerifier = new NameVerifier();

    final boolean validationResult = nameVerifier.isNameValid(name);

    assertTrue(validationResult);
  }

  @Test
  void shouldNotValidateNameWhenAllLettersAreLowerCase() {
    String name = "andrzej";
    NameVerifier nameVerifier = new NameVerifier();

    final boolean validationResult = nameVerifier.isNameValid(name);

    assertFalse(validationResult);
  }

  @Test
  void shouldNotValidateEmptyName() {
    String emptyName = "";
    NameVerifier nameVerifier = new NameVerifier();

    final boolean validationResult = nameVerifier.isNameValid(emptyName);

    assertFalse(validationResult);
  }

  @Test
  void shouldNotValidateNullName() {
    String nullName = null;
    NameVerifier nameVerifier = new NameVerifier();

    final boolean validationResult = nameVerifier.isNameValid(nullName);

    assertFalse(validationResult);
  }
}
```

## Przykład B

Przykład ten opiera się na klasie ``NameVerifier`` z przykładu A. Wykorzystuje on metodę cyklu życia testów eliminując pewne
powtórzenia.

```
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NameVerifierTest {

  private NameVerifier nameVerifier;

  @BeforeEach
  void setUp() {
    nameVerifier = new NameVerifier();
  }

  @Test
  void shouldValidateName() {
    String name = "Andrzej";

    final boolean validationResult = nameVerifier.isNameValid(name);

    assertTrue(validationResult);
  }

  @Test
  void shouldNotValidateNameWhenAllLettersAreLowerCase() {
    String name = "andrzej";

    final boolean validationResult = nameVerifier.isNameValid(name);

    assertFalse(validationResult);
  }

  @Test
  void shouldNotValidateEmptyName() {
    String emptyName = "";

    final boolean validationResult = nameVerifier.isNameValid(emptyName);

    assertFalse(validationResult);
  }

  @Test
  void shouldNotValidateNullName() {
    String nullName = null;

    final boolean validationResult = nameVerifier.isNameValid(nullName);

    assertFalse(validationResult);
  }
}
```

## Przykład C
Przykład ten pokazuje możliwe wykorzystanie wszystkich metod cyklu życia testów.

```
// not a real connection - just imitating slow connection
public class Connection {

  private boolean state;

  public void open() throws InterruptedException {
    Thread.sleep(500L); // I am a slow opening connection
    state = true;
  }

  public void close() throws InterruptedException {
    Thread.sleep(500L); // I am slow closing connection
    state = false;
  }

  public boolean isOpened() {
    return state;
  }

  public boolean isClosed() {
    return !isOpened();
  }

  public void refresh() {
    System.out.println("Refreshing the connection");
  }
}
```
```
public class StorageConnectionException extends RuntimeException {
  public StorageConnectionException(String message) {
    super(message);
  }
}
```
```
import java.util.ArrayList;
import java.util.List;

public class Storage {

  private List<Object> data = new ArrayList<>();

  private final Connection connection;

  public Storage(Connection connection) {
    this.connection = connection;
  }

  // adds value to storage data and returns number of elements
  public int addValue(final Object value) {
    if (connection.isClosed()) {
      throw new StorageConnectionException("Connection is closed so data cannot be added");
    }
    data.add(value);
    return data.size();
  }

  // tries to remove value in storage. Returns true if value was removed, false otherwise.
  public boolean removeValue(final Object value) {
    if (connection.isClosed()) {
      throw new StorageConnectionException("Connection is closed so data cannot be removed");
    }
    return data.remove(value);
  }

  public List<Object> getData() {
    if (connection.isClosed()) {
      throw new StorageConnectionException("Connection is closed so data cannot be retrieved");
    }
    return data;
  }
}
```
```
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

  private static final String EXAMPLE_DATA = "Hello";

  private static Connection connection;
  private Storage storage;

  @BeforeAll
  static void setUpTestCase() throws InterruptedException {
    connection = new Connection();
    connection.open();
  }

  @AfterAll
  static void tearDownTestCase() throws InterruptedException {
    connection.close();
  }

  @BeforeEach
  void setUp() {
    storage = new Storage(connection);
  }

  @AfterEach
  void tearDown() {
    connection.refresh();
  }

  @Test
  void shouldAddDataToStorage() {
    final int result = storage.addValue(EXAMPLE_DATA);

    assertEquals(1, result);
    assertTrue(storage.getData().contains(EXAMPLE_DATA));
  }

  @Test
  void shouldRemoveExistingData() {
    storage.addValue(EXAMPLE_DATA);

    final boolean removeResult = storage.removeValue(EXAMPLE_DATA);

    assertTrue(removeResult);
  }

  @Test
  void shouldNotRemoveNonExistingData() {
    storage.addValue(EXAMPLE_DATA);

    final boolean removeResult = storage.removeValue("NotExistingData");

    assertFalse(removeResult);
    assertEquals(storage.getData().size(), 1);
  }

}
```

## Przykład D 
Przykład ten pokazuje wykorzystanie asercji ``assertIterableEquals`` i ``assertNotSame``.

```
public class ListUtils {

  public List<Integer> multiplyInputs(final List<Integer> input, final int multiplier) {
    final List<Integer> results = new ArrayList<>();
    for (final Integer value : input) {
      results.add(value * multiplier);
    }
    return results;
  }
}
```
```
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListUtilsTest {

  private final ListUtils listUtils = new ListUtils(); // do inicjalizacji obiektu NIE musimy wykorzystywać metody z `@BeforeEach`.

  @Test
  void shouldTripleInput() {
    final List<Integer> inputs = List.of(4, 2, 7);
    final List<Integer> expectedOutput = List.of(12, 6, 21);
    final int multiplier = 3;

    final List<Integer> actualResults = listUtils.multiplyInputs(inputs, multiplier);

    assertIterableEquals(expectedOutput, actualResults, "Lists do not match in terms of values or size");
    assertNotSame(inputs, actualResults, "The input list was modified while it should not be touched");
  }
}
```

## Przykład E 
Przykład wykorzystuje ``assertLinesMatch`` oraz adnotacje ``@DisplayName`` i ``@Disabled``.

```
import java.util.List;
import java.util.stream.Collectors;

public class StringUtils {

  public List<String> toUpperCase(final List<String> inputs) {
    return inputs.stream()
        .map(String::toUpperCase)
        .collect(Collectors.toUnmodifiableList());
  }

  public List<String> toLowerCase(final List<String> inputs) {
    return inputs.stream()
        .map(String::toLowerCase)
        .collect(Collectors.toUnmodifiableList());
  }

  public List<String> toSnakeCase(final List<String> inputs) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
```
```
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

class StringUtilsTest {

  private StringUtils stringUtils;

  @BeforeEach
  void setUp() {
    stringUtils = new StringUtils();
  }

  @Test
  void shouldUpperCaseInputs() {
    final List<String> inputs = List.of("Hello", "From", "sda");
    final List<String> exepectedOutput = List.of("HELLO", "FROM", "SDA");

    final List<String> actualOutputs = stringUtils.toUpperCase(inputs);

    assertLinesMatch(exepectedOutput, actualOutputs);
  }

  @Test
  @DisplayName("Test that checks toLowerCase method behavior")
  void shouldLowerCaseInputs() {
    final List<String> inputs = List.of("Hello", "From", "SDA");
    final List<String> exepectedOutput = List.of("hello", "from", "sda");

    final List<String> actualOutputs = stringUtils.toLowerCase(inputs);

    assertLinesMatch(exepectedOutput, actualOutputs);
  }

  @Test
  @Disabled("waiting for actual method implementation. TDD approach")
  void shouldSnakeCaseInputs() {
    final List<String> inputs = List.of("helloFrom", "testExamples");
    final List<String> exepectedOutput = List.of("hello_from", "test_example");

    final List<String> actualOutputs = stringUtils.toLowerCase(inputs);

    assertLinesMatch(exepectedOutput, actualOutputs);
  }

}
```

## Przykład F
Przykład pokazuje wykorzystanie asercji grupującej - ``assertAll``.

```
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
  private String prefix;
  private String firstName;
  private String lastName;
}
```
```
public class PersonFactory {

  public Person createPerson(final String firstName, final String lastName) {
    final String prefix = firstName.endsWith("a") ? "Ms" : "Mr";
    return new Person(prefix, firstName, lastName);
  }
}
```
```
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonFactoryTest {

  private static final String MALE_NAME = "Andrzej";
  private static final String FEMALE_NAME = "Ala";
  private static final String SURNAME = "Nowak";
  private static final String MALE_PREFIX = "Mr";
  private static final String FEMALE_PREFIX = "Ms";

  private final PersonFactory personFactory = new PersonFactory();

  @Test
  void shouldCreateMalePerson() {
    final Person person = personFactory.createPerson(MALE_NAME, SURNAME);

    assertAll(
        () -> assertEquals(SURNAME, person.getLastName()),
        () -> assertEquals(MALE_NAME, person.getFirstName()),
        () -> assertEquals(MALE_PREFIX, person.getPrefix())
    );
  }

  @Test
  void shouldCreateFemalePerson() {
    final Person person = personFactory.createPerson(FEMALE_NAME, SURNAME);

    assertAll(
        () -> assertEquals(SURNAME, person.getLastName()),
        () -> assertEquals(FEMALE_NAME, person.getFirstName()),
        () -> assertEquals(FEMALE_PREFIX, person.getPrefix())
    );
  }
}
```