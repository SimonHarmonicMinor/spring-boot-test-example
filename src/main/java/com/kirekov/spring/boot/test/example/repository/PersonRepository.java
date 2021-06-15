package com.kirekov.spring.boot.test.example.repository;

import com.kirekov.spring.boot.test.example.entity.Person;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * DAO for {@linkplain Person}.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

  boolean existsByFirstNameAndLastName(String firstName, String lastName);

  @Query("select distinct p.lastName from Person p")
  Set<String> findAllLastNames();
}
