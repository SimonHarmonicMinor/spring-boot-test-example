package com.kirekov.spring.boot.test.example.service.person;

import com.kirekov.spring.boot.test.example.entity.Person;
import com.kirekov.spring.boot.test.example.exception.ValidationFailedException;

/**
 * {@linkplain Person} validation service.
 */
public interface PersonValidateService {

  /**
   * Checks whether the new user can be created.
   *
   * @param firstName first name
   * @param lastName  last name
   * @throws ValidationFailedException if user creation is not allowed
   */
  void checkUserCreation(String firstName, String lastName);
}
