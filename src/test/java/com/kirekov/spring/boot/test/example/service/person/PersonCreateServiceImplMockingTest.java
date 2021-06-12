package com.kirekov.spring.boot.test.example.service.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.kirekov.spring.boot.test.example.entity.Person;
import com.kirekov.spring.boot.test.example.exception.ValidationFailedException;
import com.kirekov.spring.boot.test.example.repository.PersonRepository;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@linkplain PersonCreateServiceImpl} tests with mocks.
 */
@DisplayName("PersonCreateServiceImpl: tests with mocks")
class PersonCreateServiceImplMockingTest {

  private final PersonValidateService personValidateService = mock(PersonValidateService.class);
  private final PersonRepository personRepository = mock(PersonRepository.class);
  private final PersonCreateService service = new PersonCreateServiceImpl(personValidateService,
      personRepository);

  @Test
  @DisplayName("Should fail user creation if validation does not pass")
  void shouldFailUserCreation() {
    final var firstName = "Jack";
    final var lastName = "Black";
    doThrow(new ValidationFailedException(""))
        .when(personValidateService)
        .checkUserCreation(firstName, lastName);
    assertThrows(ValidationFailedException.class, () -> service.createPerson(firstName, lastName));
  }

  @Test
  @DisplayName("Should create new user successfully")
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