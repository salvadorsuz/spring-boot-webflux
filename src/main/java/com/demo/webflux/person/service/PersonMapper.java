package com.demo.webflux.person.service;

import com.demo.webflux.person.persistence.entity.PersonEntity;
import com.demo.webflux.person.Person;

public class PersonMapper {

  private PersonMapper() {}

  static Person toPerson(PersonEntity personEntity) {
    return new Person(
        personEntity.getId(),
        personEntity.getFirstName(),
        personEntity.getLastName(),
        personEntity.getCountry(),
        personEntity.getAge());
  }

  static PersonEntity toPersonEntity(Person person) {
    return new PersonEntity(
        person.getId(),
        person.getFirstName(),
        person.getLastName(),
        person.getCountry(),
        person.getAge());
  }

}
