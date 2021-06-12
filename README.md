# Spring Boot Test Example

This is small template project that shows the power
of [Spring Boot Test](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html)
.

Those features are included.

1. Service layer tests + [Mocks](https://www.baeldung.com/mockito-series)
1. Service layer tests + [H2 Database](https://www.h2database.com/html/main.html)
1. Service layer tests
    + [Testcontainers PostgreSQL](https://www.testcontainers.org/modules/databases/postgres/)
1. Repository tests + [H2 Database](https://www.h2database.com/html/main.html)
1. Repository tests
    + [Testcontainers PostgreSQL](https://www.testcontainers.org/modules/databases/postgres/)
1. Controller tests + [WebMvcMock](https://spring.io/guides/gs/testing-web/)
1. Controller tests + [TestRestTemplate](https://www.baeldung.com/spring-boot-testresttemplate)

# Status

[![Build Status](https://travis-ci.com/SimonHarmonicMinor/spring-boot-test-example.svg?branch=master)](https://travis-ci.com/SimonHarmonicMinor/spring-boot-test-example)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=SimonHarmonicMinor_spring-boot-test-example&metric=alert_status)](https://sonarcloud.io/dashboard?id=SimonHarmonicMinor_spring-boot-test-example)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=SimonHarmonicMinor_spring-boot-test-example&metric=coverage)](https://sonarcloud.io/dashboard?id=SimonHarmonicMinor_spring-boot-test-example)

# Service layer

## Mocks

The most obvious way to test services is to mock all dependencies.
[Mockito](https://site.mockito.org/) comes in rescue in that case.

Assume that we have a simple `PersonCreateService`.

```java

@Service
@RequiredArgsConstructor
public class PersonCreateService {

  private final PersonValidateService personValidateService;
  private final PersonRepository personRepository;

  @Override
  @Transactional
  public PersonDTO createPerson(String firstName, String lastName) {
    personValidateService.checkUserCreation(firstName, lastName);
    final var createdPerson = personRepository.saveAndFlush(
        new Person()
            .setFirstName(firstName)
            .setLastName(lastName)
    );
    return DTOConverters.toPersonDTO(createdPerson);
  }
}
```

Let's write a simple unit test. What if validation does not pass?

```java
class PersonCreateServiceImplMockingTest {

  private final PersonValidateService personValidateService = mock(PersonValidateService.class);
  private final PersonRepository personRepository = mock(PersonRepository.class);
  private final PersonCreateService service = new PersonCreateServiceImpl(personValidateService,
      personRepository);

  @Test
  void shouldFailUserCreation() {
    final var firstName = "Jack";
    final var lastName = "Black";
    doThrow(new ValidationFailedException(""))
        .when(personValidateService)
        .checkUserCreation(firstName, lastName);
    assertThrows(ValidationFailedException.class, () -> service.createPerson(firstName, lastName));
  }
}
```

OK, this test is pretty easy. Let us thing about something more complicated. What if user creation
passes successfully? Well, that requires a bit more determination.

Firstly, we need to mock `PersonRepository`
so `saveAndFlush` returns the new `Person` instance with filled `id` field.

Secondly, we need to test that the result `PersonDTO` contains the expected information.

```java
class PersonCreateServiceImplMockingTest {
  // initialization... 

  @Test
  void shouldCreateNewUser() {
    final var firstName = "Lisa";
    final var lastName = "Green";
    when(personRepository.saveAndFlush(any()))
        .thenAnswer(invocation -> {
          Person person = invocation.getArgument(0);
          assert Objects.equals(person.getFirstName(), firstName);
          assert Objects.equals(person.getLastName(), lastName);
          return person.setId(1L);
        });
    final var personDTO = service.createPerson(firstName, lastName);
    assertEquals(personDTO.getFirstName(), firstName);
    assertEquals(personDTO.getLastName(), lastName);
    assertNotNull(personDTO.getId());
  }
}
```

Now we can recognize that something isn't right. 
It's just one operation, but the test has already become so complicated 
that it's difficult to understand the flow.

What even worse that mocks just cannot validate particular situations.

TODO: finish