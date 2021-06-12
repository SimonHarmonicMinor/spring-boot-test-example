package com.kirekov.spring.boot.test.example.service.person;

import com.kirekov.spring.boot.test.example.converters.DTOConverters;
import com.kirekov.spring.boot.test.example.dto.PersonDTO;
import com.kirekov.spring.boot.test.example.entity.Person;
import com.kirekov.spring.boot.test.example.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonCreateServiceImpl implements
    PersonCreateService {

  private final PersonValidateService personValidateService;
  private final PersonRepository personRepository;

  @Override
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
