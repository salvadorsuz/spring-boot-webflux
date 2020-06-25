package com.demo.webflux.person;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "All details about the Person")
public class Person {

  @Schema(description = "Id of the person. UUID format", example = "a4f66fe5-7c1b-4bcf-89b4-93d8fcbc52a4")
  private UUID id;

  @Schema(description = "First name of the person", example = "Juan")
  private String firstName;

  @Schema(description = "Last name of the person", example = "Garcia")
  private String lastName;

  @Schema(description = "Country of the person", example = "Spain")
  private String country;

  @Schema(description = "Age of the person")
  private int age;

  public Person(Person person, UUID id) {
    this.id = id;
    this.firstName = person.firstName;
    this.lastName = person.lastName;
    this.country = person.country;
    this.age = person.age;
  }
}
