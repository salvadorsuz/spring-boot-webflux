package com.demo.webflux.person.web;

import com.demo.webflux.person.Person;
import com.demo.webflux.person.persistence.entity.PersonEntity;
import com.demo.webflux.person.persistence.repository.PersonRepository;
import com.demo.webflux.person.service.PersonManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
@SpringBootTest
class PersonFunctionalIntegrationTest {

    @Autowired
    private PersonRouter router;

    @Autowired
    private PersonHandler personHandler;

    @Autowired
    private PersonManager personManager;

    @MockBean
    private PersonRepository personRepository;

    private static final String NAME = "Juan";
    private static final String COUNTRY = "Spain";
    private static final Integer AGE = 30;

    private Person person = null;

    private PersonEntity personEntity = null;

    private UUID uuid = null;

    private WebTestClient testClient = null;

    @BeforeEach
    public void init() {
        uuid = UUID.randomUUID();
        person = Person.builder().id(uuid).firstName(NAME).age(AGE).country(COUNTRY).build();
        personEntity = PersonEntity.builder().id(uuid).firstName(NAME).age(AGE).country(COUNTRY).build();

    }

    @Nested
    @DisplayName("Test plan GET Person by ID")
    class WhenRouteGetPersonById {

        @BeforeEach
        public void init() {
            testClient = WebTestClient
                    .bindToRouterFunction(router.routeGetPersonById(personHandler))
                    .build();
        }

        @Test
        @DisplayName("Test plan GET Person by persisted ID")
        public void givenPersonId_whenGetPersonById_thenCorrectPerson() {
            given(personRepository.findById(uuid)).willReturn(Mono.just(personEntity));

            testClient.get()
                    .uri("/people/"+uuid)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(Person.class)
                    .isEqualTo(person);
        }

        @Test
        @DisplayName("Test plan GET Person by NOT persisted ID")
        public void givenNotValidPersonId_whenGetPersonById_thenNotFound() {
            given(personRepository.findById(uuid)).willReturn(Mono.empty());

            testClient.get()
                    .uri("/people/"+uuid)
                    .exchange()
                    .expectStatus()
                    .isNotFound();

        }
    }

    @Nested
    @DisplayName("Test plan GET all Person ")
    class WhenRouteGetAllPerson {

        @BeforeEach
        public void init() {
            testClient = WebTestClient
                    .bindToRouterFunction(router.routeGetAllPerson(personHandler))
                    .build();
        }

        @Test
        @DisplayName("Test plan GET all Person ")
        public void givenAllPerson_whenGetAllPerson_thenAllPerson() {
            given(personRepository.findAll()).willReturn(Flux.just(personEntity));

            testClient.get()
                    .uri("/people")
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBodyList(Person.class)
                    .isEqualTo(List.of(person));
        }

        @Test
        @DisplayName("Test plan GET no Person ")
        public void givenNoPerson_whenGetAllPerson_thenNoPerson() {
            given(personRepository.findAll()).willReturn(Flux.empty());

            testClient.get()
                    .uri("/people")
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBodyList(Person.class)
                    .isEqualTo(Collections.EMPTY_LIST);
        }
    }


    @Nested
    @DisplayName("Test plan POST new Person ")
    class WhenRoutePostPerson {

        @BeforeEach
        public void init() {
            testClient = WebTestClient
                    .bindToRouterFunction(router.routeSavePerson(personHandler))
                    .build();
        }

        @Test
        @DisplayName("Test plan POST new Person")
        public void givenNewPerson_whenPostPerson_thenCreatedPerson() {
            given(personRepository.save(any())).willReturn(Mono.just(personEntity));

            testClient.post()
                    .uri("/people")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromPublisher(Mono.just(person), Person.class))
                    .exchange()
                    .expectStatus().isCreated()
                    .expectHeader().exists("Location")
                    .expectBody(Person.class);

        }
    }

    @Nested
    @DisplayName("Test plan PUT Person ")
    class WhenRoutePutPerson {

        @BeforeEach
        public void init() {
            testClient = WebTestClient
                    .bindToRouterFunction(router.routeModifyPerson(personHandler))
                    .build();
        }


        @Test
        @DisplayName("Test plan PUT persisted Person")
        public void givenPerson_whenPutPerson_thenOkPerson() {
            given(personRepository.findById(uuid)).willReturn(Mono.just(personEntity));
            given(personRepository.save(any())).willReturn(Mono.just(personEntity));

            testClient.put()
                    .uri("/people/"+uuid)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromPublisher(Mono.just(person), Person.class))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(Person.class);

        }

        @Test
        @DisplayName("Test plan PUT not persisted Person")
        public void givenNotValidPerson_whenPutPerson_thenNotFound() {
            given(personRepository.findById(uuid)).willReturn(Mono.empty());

            testClient.put()
                    .uri("/people/"+uuid)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromPublisher(Mono.just(person), Person.class))
                    .exchange()
                    .expectStatus().isNotFound();

        }


    }

    @Nested
    @DisplayName("Test plan DELETE Person by ID")
    class WhenRouteDeletePersonById {

        @BeforeEach
        public void init() {
            testClient = WebTestClient
                    .bindToRouterFunction(router.routeDeletePerson(personHandler))
                    .build();
        }

        @Test
        @DisplayName("Test plan DELETE Person by persisted ID")
        public void givenPersonId_whenDeletePersonById_thenNoContent() {
            given(personRepository.findById(uuid)).willReturn(Mono.just(personEntity));
            given(personRepository.delete(personEntity)).willReturn(Mono.empty());

            testClient.delete()
                    .uri("/people/"+uuid)
                    .exchange()
                    .expectStatus().isNoContent();
        }

        @Test
        @DisplayName("Test plan DELETE Person by NOT persisted ID")
        public void givenNotValidPersonId_whenDeletePersonById_thenNotFound() {
            given(personRepository.findById(uuid)).willReturn(Mono.empty());

            testClient.delete()
                    .uri("/people/"+uuid)
                    .exchange()
                    .expectStatus().isNotFound();

        }
    }
}