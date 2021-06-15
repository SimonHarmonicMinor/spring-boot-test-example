package com.kirekov.spring.boot.test.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kirekov.spring.boot.test.example.entity.Person;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("PersonRepository: findAllLastNames tests")
class PersonRepositoryDataJpaTest {

  @Autowired
  private PersonRepository personRepository;

  @Test
  @DisplayName("Should return all last names")
  void shouldReturnAlLastNames() {
    personRepository.saveAndFlush(new Person().setFirstName("John").setLastName("Brown"));
    personRepository.saveAndFlush(new Person().setFirstName("Kyle").setLastName("Green"));
    personRepository.saveAndFlush(new Person().setFirstName("Paul").setLastName("Brown"));

    assertEquals(Set.of("Brown", "Green"), personRepository.findAllLastNames());
  }
}