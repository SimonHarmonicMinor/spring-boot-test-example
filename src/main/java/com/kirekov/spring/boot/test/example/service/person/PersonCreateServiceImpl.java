package com.kirekov.spring.boot.test.example.service.person;

import com.kirekov.spring.boot.test.example.converters.DTOConverters;
import com.kirekov.spring.boot.test.example.dto.PersonDTO;
import com.kirekov.spring.boot.test.example.entity.Person;
import com.kirekov.spring.boot.test.example.repository.PersonRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonCreateServiceImpl implements PersonCreateService {

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

  @Override
  @Transactional
  public List<PersonDTO> createFamily(Iterable<String> firstNames, String lastName) {
    final var people = new ArrayList<PersonDTO>();
    firstNames.forEach(firstName -> people.add(createPerson(firstName, lastName)));
    return people;
  }
}
