package com.kirekov.spring.boot.test.example.dto;

import java.time.ZonedDateTime;
import lombok.Value;

/**
 * PersonDTO.
 */
@Value
public class PersonDTO {

  Long id;
  String firstName;
  String lastName;
  ZonedDateTime dateCreated;
}
