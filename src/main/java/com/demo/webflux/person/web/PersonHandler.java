package com.demo.webflux.person.web;

import com.demo.webflux.exception.NotFoundException;
import com.demo.webflux.person.Person;
import com.demo.webflux.person.service.PersonManager;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Component
public class PersonHandler {

  private final PersonManager personManager;

  public PersonHandler(PersonManager personManager) {
    this.personManager = personManager;
  }

  public Mono<ServerResponse> get(ServerRequest request) {
      final UUID id = UUID.fromString(request.pathVariable("id"));
      final Mono<Person> person = personManager.findById(id);
      return person
            .flatMap(p -> ok().contentType(APPLICATION_JSON).body(fromPublisher(person, Person.class)))
            .switchIfEmpty(notFound().build())
            .onErrorResume(PersonHandler::errorFallback);
  }

  public Mono<ServerResponse> all(ServerRequest request) {
      final Flux<Person> people = personManager.findAll();
      return ok().contentType(APPLICATION_JSON).body(fromPublisher(people, Person.class));
  }

  public Mono<ServerResponse> put(ServerRequest request) {
      final UUID id = UUID.fromString(request.pathVariable("id"));
      final Mono<Person> person = request.bodyToMono(Person.class).flatMap(p -> personManager.update(p, id));

      return person.flatMap(p -> ok().contentType(APPLICATION_JSON).body(fromValue(p)))
              .onErrorResume(PersonHandler::errorFallback);
  }

  public Mono<ServerResponse> post(ServerRequest request) {
      final UUID id = UUID.randomUUID();
      final Mono<Person> person = request.bodyToMono(Person.class).flatMap(p -> personManager.save(p, id));

      return person.flatMap(p -> created(UriComponentsBuilder.fromPath("people/" +id).build().toUri())
                        .contentType(APPLICATION_JSON).body(fromValue(p)))
              .onErrorResume(PersonHandler::errorFallback);
  }

  public Mono<ServerResponse> delete(ServerRequest request) {
      final UUID id = UUID.fromString(request.pathVariable("id"));
      return personManager.delete(id)
            .flatMap(p -> noContent().build())
            .onErrorResume(PersonHandler::errorFallback);
  }

  private static Mono<? extends ServerResponse> errorFallback(Throwable throwable) {
      Mono<ServerResponse> out;
      if (throwable instanceof NotFoundException) {
          out = notFound().build();
      } else {
          out = badRequest().bodyValue(throwable.getMessage());
      }
      return out;
  }

}
