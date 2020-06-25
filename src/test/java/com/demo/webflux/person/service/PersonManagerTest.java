package com.demo.webflux.person.service;

import com.demo.webflux.exception.NotFoundException;
import com.demo.webflux.person.Person;
import com.demo.webflux.person.persistence.entity.PersonEntity;
import com.demo.webflux.person.persistence.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@DisplayName("PersonManager tests ")
class PersonManagerTest {

    private static final String NAME = "Juan";
    private static final String COUNTRY = "Spain";
    private static final Integer AGE = 30;

    @InjectMocks
    private PersonManager personManager;

    @Mock
    private PersonRepository personRepository;

    private Person person = null;

    private PersonEntity personEntity = null;

    private UUID uuid = null;

    @BeforeEach
    public void init() {
        uuid = UUID.randomUUID();
        person = Person.builder().id(uuid).firstName(NAME).age(AGE).country(COUNTRY).build();
        personEntity = PersonEntity.builder().id(uuid).firstName(NAME).age(AGE).country(COUNTRY).build();
    }

    @Nested
    @DisplayName("Test plan Find Person by ID")
    class WhenFindById {

        @Test
        @DisplayName("Test plan Find Person by persisted ID")
        public void givenPersonId_whenFindById_thenCorrectPerson() {
            //given
            given(personRepository.findById(uuid)).willReturn(Mono.just(personEntity));

            //when
            Mono<Person> personResult = personManager.findById(uuid);

            //then
            StepVerifier.create(personResult)
                    .expectNext(person)
                    .expectComplete()
                    .verify();
        }

        @Test
        @DisplayName("Test plan Find Person by not persisted ID")
        public void givenNotValidPersonId_whenFindById_thenErrorNotFound() {
            //given
            given(personRepository.findById(uuid)).willReturn(Mono.empty());

            //when
            Mono<Person> personResult = personManager.findById(uuid);

            //then
            StepVerifier.create(personResult)
                    .expectError(NotFoundException.class)
                    .verify();
        }
    }


}