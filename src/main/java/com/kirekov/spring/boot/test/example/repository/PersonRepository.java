package com.kirekov.spring.boot.test.example.repository;

import com.kirekov.spring.boot.test.example.entity.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DAO for {@linkplain Person}.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

  boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
