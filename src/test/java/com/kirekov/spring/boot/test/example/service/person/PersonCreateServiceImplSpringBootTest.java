package com.kirekov.spring.boot.test.example.service.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;

import com.kirekov.spring.boot.test.example.entity.Person;
import com.kirekov.spring.boot.test.example.exception.ValidationFailedException;
import com.kirekov.spring.boot.test.example.repository.PersonRepository;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase
@DisplayName("PersonCreateServiceImpl: tests with H2 database and whole Spring context")
class PersonCreateServiceImplSpringBootTest {

  @Autowired
  private PersonRepository personRepository;
  @MockBean
  private PersonValidateService personValidateService;
  @Autowired
  private PersonCreateService personCreateService;

  @BeforeEach
  void init() {
    personRepository.deleteAll();
  }

  @Test
  @DisplayName("Should create one person")
  void shouldCreateOnePerson() {
    final var people = personCreateService.createFamily(
        List.of("Simon"),
        "Kirekov"
    );
    assertEquals(1, people.size());
    final var person = people.get(0);
    assertEquals("Simon", person.getFirstName());
    assertEquals("Kirekov", person.getLastName());
    assertTrue(person.getDateCreated().isBefore(ZonedDateTime.now()));
  }

  @Test
  @DisplayName("Should rollback if any user is not validated")
  void shouldRollbackIfAnyUserIsNotValidated() {
    doThrow(new ValidationFailedException(""))
        .when(personValidateService)
        .checkUserCreation("John", "Brown");
    assertThrows(ValidationFailedException.class, () -> personCreateService.createFamily(
        List.of("Matilda", "Vasya", "John"),
        "Brown"
    ));
    assertEquals(0, personRepository.count());
  }

  @Test
  @DisplayName("Should rollback if the only user is not validated")
  void shouldRollbackIfOneUserIsNotValidated() {
    doAnswer(invocation -> {
      final String lastName = invocation.getArgument(1);
      final var exists = personRepository.exists(Example.of(new Person().setLastName(lastName)));
      System.out.println("Person with " + lastName + " exists: " + exists);
      if (exists) {
        throw new ValidationFailedException("Person with " + lastName + " already exists");
      }
      return null;
    }).when(personValidateService).checkUserCreation(any(), any());
    personRepository.saveAndFlush(new Person().setFirstName("Alice").setLastName("Purple"));
    assertThrows(ValidationFailedException.class, () -> personCreateService.createFamily(
        List.of("Matilda"),
        "Purple"
    ));
    assertEquals(1, personRepository.count());
  }
}