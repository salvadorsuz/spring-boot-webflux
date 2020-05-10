package com.demo.webflux.person.persistence.repository;

import com.demo.webflux.person.persistence.entity.PersonEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonRepository extends ReactiveMongoRepository<PersonEntity, UUID> {
}
