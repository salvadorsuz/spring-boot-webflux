package com.demo.webflux.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Person {

  private UUID id;
  private String firstName;
  private String lastName;
  private String country;
  private int age;

  public Person(Person person, UUID id) {
    this.id = id;
    this.firstName = person.firstName;
    this.lastName = person.lastName;
    this.country = person.country;
    this.age = person.age;
  }
}
