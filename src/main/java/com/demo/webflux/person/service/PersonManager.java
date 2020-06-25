package com.demo.webflux.person.service;

import com.demo.webflux.exception.NotFoundException;
import com.demo.webflux.person.Person;
import com.demo.webflux.person.persistence.entity.PersonEntity;
import com.demo.webflux.person.persistence.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

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
      return personRepository.findById(id).map(PersonMapper::toPerson)
              .switchIfEmpty(Mono.error(new NotFoundException("Not found Person with uuid:"+id)));
  }

  public Mono<Person> save(final Person person, final UUID id) {
      Mono<PersonEntity> personNew =  Mono.just(person).map(p -> new Person(p, id)).map(PersonMapper::toPersonEntity);

      return personNew.flatMap(p -> personRepository.save(p).thenReturn(PersonMapper.toPerson(p)));
  }

  public Mono<Person> update(final Person person, final UUID uuid) {
      final Mono<Person> personMon = Mono.just(person).map(p -> new Person(p, uuid));

      return this.findById(uuid)
              .flatMap(old -> personMon.flatMap(p -> this.save(p, uuid)));
  }

  public Mono<UUID> delete(final UUID uuid) {
      return this.findById(uuid)
              .flatMap(p -> this.personRepository.delete(PersonMapper.toPersonEntity(p)).thenReturn(uuid));
  }

}
