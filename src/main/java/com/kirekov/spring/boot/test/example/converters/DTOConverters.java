package com.kirekov.spring.boot.test.example.converters;

import static lombok.AccessLevel.PRIVATE;

import com.kirekov.spring.boot.test.example.dto.PersonDTO;
import com.kirekov.spring.boot.test.example.entity.Person;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class DTOConverters {

  public static PersonDTO toPersonDTO(Person person) {
    return new PersonDTO(
        person.getId(),
        person.getFirstName(),
        person.getLastName(),
        person.getDateCreated()
    );
  }
}
