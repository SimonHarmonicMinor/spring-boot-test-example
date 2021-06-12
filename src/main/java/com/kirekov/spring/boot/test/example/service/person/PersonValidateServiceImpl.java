package com.kirekov.spring.boot.test.example.service.person;

import com.kirekov.spring.boot.test.example.exception.ValidationFailedException;
import com.kirekov.spring.boot.test.example.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class PersonValidateServiceImpl implements
    PersonValidateService {

  private final PersonRepository personRepository;

  @Override
  @Transactional(readOnly = true)
  public void checkUserCreation(String firstName, String lastName) {
    if (personRepository.existsByFirstNameAndLastName(firstName, lastName)) {
      throw new ValidationFailedException("Such person already exists");
    }
  }
}
