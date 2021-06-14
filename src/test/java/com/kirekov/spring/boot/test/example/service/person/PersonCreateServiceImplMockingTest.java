package com.kirekov.spring.boot.test.example.service.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kirekov.spring.boot.test.example.entity.Person;
import com.kirekov.spring.boot.test.example.exception.ValidationFailedException;
import com.kirekov.spring.boot.test.example.repository.PersonRepository;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
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

  @Test
  @DisplayName("Should create family")
  void shouldCreateFamily() {
    final var firstNames = List.of("John", "Samantha", "Kyle");
    final var lastName = "Purple";
    final var idHolder = new AtomicLong(0);
    when(personRepository.saveAndFlush(any()))
        .thenAnswer(invocation -> {
          Person person = invocation.getArgument(0);
          assert firstNames.contains(person.getFirstName());
          assert Objects.equals(person.getLastName(), lastName);
          return person.setId(idHolder.incrementAndGet());
        });
    final var people = service.createFamily(firstNames, lastName);
    for (int i = 0; i < people.size(); i++) {
      final var personDTO = people.get(i);
      assertEquals(personDTO.getFirstName(), firstNames.get(i));
      assertEquals(personDTO.getLastName(), lastName);
      assertNotNull(personDTO.getId());
    }
    verify(personValidateService, times(3)).checkUserCreation(any(), any());
    verify(personRepository, times(3)).saveAndFlush(any());
  }

}