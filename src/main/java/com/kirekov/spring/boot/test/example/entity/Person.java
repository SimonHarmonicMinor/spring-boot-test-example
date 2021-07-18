package com.kirekov.spring.boot.test.example.entity;


import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Getter;

/**
 * Person table.
 */
@Entity
@Table(name = "person")
@Getter
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "date_created")
  private ZonedDateTime dateCreated;

  @PrePersist
  @PreUpdate
  void setCurrentTimeAsDateCreated() {
    dateCreated = ZonedDateTime.now();
  }

  public Person setId(Long id) {
    this.id = id;
    return this;
  }

  public Person setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public Person setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }
}
