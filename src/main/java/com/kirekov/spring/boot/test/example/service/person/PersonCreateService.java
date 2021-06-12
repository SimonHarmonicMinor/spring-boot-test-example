package com.kirekov.spring.boot.test.example.service.person;

import com.kirekov.spring.boot.test.example.dto.PersonDTO;
import com.kirekov.spring.boot.test.example.entity.Person;
import com.kirekov.spring.boot.test.example.exception.ValidationFailedException;
import java.util.List;

public interface PersonCreateService {

  /**
   * Creates new {@linkplain Person}.
   *
   * @param firstName first name
   * @param lastName  last name
   * @return created person info
   * @throws ValidationFailedException if person cannot be created
   */
  PersonDTO createPerson(String firstName, String lastName);

  /**
   * Creates multiple {@linkplain Person} with the provided firstNames and lastName.
   *
   * @param firstNames first names
   * @param lastName   last name
   * @return created people list in the same order as provided
   * @throws ValidationFailedException if any person cannot be created
   */
  List<PersonDTO> createFamily(Iterable<String> firstNames, String lastName);
}
