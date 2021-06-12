package com.kirekov.spring.boot.test.example.service.person;

import com.kirekov.spring.boot.test.example.dto.PersonDTO;
import com.kirekov.spring.boot.test.example.entity.Person;
import com.kirekov.spring.boot.test.example.exception.ValidationFailedException;

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
}
