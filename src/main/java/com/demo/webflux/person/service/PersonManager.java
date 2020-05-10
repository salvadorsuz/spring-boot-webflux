package com.demo.webflux.person.service;

import com.demo.webflux.person.Person;
import com.demo.webflux.person.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class PersonManager {

  private final PersonRepository personRepository;

  @Autowired
  public PersonManager(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public Flux<Person> findAll() {
    return personRepository.findAll().map(PersonMapper::toPerson);
  }

  public Mono<Person> findById(final UUID id) {
    return personRepository.findById(id).map(PersonMapper::toPerson).switchIfEmpty(Mono.empty());
  }

  public Mono<Person> save(Person person) {
    return Mono.fromSupplier(
        () -> {
          personRepository
              .save(PersonMapper.toPersonEntity(person))
              .and(personRepository.save(PersonMapper.toPersonEntity(person)))
              .subscribe();
          return person;
        });
  }

  public Mono<Person> update(Person old, Person updated) {
    return Mono.fromSupplier(
        () -> {
          personRepository
              .save(PersonMapper.toPersonEntity(updated))
              .and(personRepository.delete(PersonMapper.toPersonEntity(old)))
              .and(personRepository.save(PersonMapper.toPersonEntity(updated)))
              .subscribe();
          return updated;
        });
  }

  public Mono<Void> delete(Person person) {
    return Mono.fromSupplier(
        () -> {
          personRepository
              .delete(PersonMapper.toPersonEntity(person))
              .and(personRepository.delete(PersonMapper.toPersonEntity(person)))
              .subscribe();
          return null;
        });
  }
}
