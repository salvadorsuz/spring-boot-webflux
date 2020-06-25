package com.demo.webflux.person.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonEntity {

  @Id
  private UUID id;
  private String firstName;
  private String lastName;
  private String country;
  private int age;
}
