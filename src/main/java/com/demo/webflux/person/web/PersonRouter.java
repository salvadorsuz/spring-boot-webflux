package com.demo.webflux.person.web;

import com.demo.webflux.person.Person;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class PersonRouter {

    /*
  @RouterOperations({
          @RouterOperation(method = GET, path = "/people/{id}", beanClass = PersonHandler.class, beanMethod = "get"),
          @RouterOperation(method = GET, path = "/people", beanClass = PersonHandler.class, beanMethod = "all"),
          @RouterOperation(method = POST, path = "/people", beanClass = PersonHandler.class, beanMethod = "post"),
          @RouterOperation(method = PUT, path = "/people", beanClass = PersonHandler.class, beanMethod = "put"),
          @RouterOperation(method = DELETE, path = "/people/{id}", beanClass = PersonHandler.class, beanMethod = "delete") })
  @Bean
  public RouterFunction<ServerResponse> route(PersonHandler personHandler) {
    return RouterFunctions.route(GET("/people/{id}").and(accept(APPLICATION_JSON)), personHandler::get)
        .andRoute(GET("/people").and(accept(APPLICATION_JSON)), personHandler::all)
        .andRoute(POST("/people").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), personHandler::post)
        .andRoute(PUT("/people/{id}").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), personHandler::put)
        .andRoute(DELETE("/people/{id}"), personHandler::delete);
  }

     */
    @Bean
    @RouterOperation(operation = @Operation(operationId = "findPersonById", summary = "Find person by ID", tags = { "Person" },
                        parameters = { @Parameter(in = ParameterIn.PATH, name = "id", description = "Person Id") },
                        responses = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Person.class))),
                                @ApiResponse(responseCode = "404", description = "Person not found") }))
    public RouterFunction<ServerResponse> routeGetPersonById(PersonHandler personHandler) {
        return RouterFunctions.route(GET("/people/{id}").and(accept(APPLICATION_JSON)), personHandler::get);
    }

    @Bean
    @RouterOperation(operation = @Operation(operationId = "findAllPerson", summary = "Find all person", tags = { "Person" },
                        responses = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Person.class)))}))
    public RouterFunction<ServerResponse> routeGetAllPerson(PersonHandler personHandler) {
        return RouterFunctions.route(GET("/people").and(accept(APPLICATION_JSON)), personHandler::all);
    }

    @Bean
    @RouterOperation(operation = @Operation(operationId = "savePerson", summary = "Save a person", tags = { "Person" },
                        requestBody = @RequestBody(description = "New Person", content = @Content(schema = @Schema(implementation = Person.class))),
                        responses = { @ApiResponse(responseCode = "201", description = "successful operation", content = @Content(schema = @Schema(implementation = Person.class))) }))
    public RouterFunction<ServerResponse> routeSavePerson(PersonHandler personHandler) {
        return RouterFunctions.route(POST("/people").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), personHandler::post);
    }

    @Bean
    @RouterOperation(operation = @Operation(operationId = "modifyPerson", summary = "Modify a person", tags = { "Person" },
                        parameters = { @Parameter(in = ParameterIn.PATH, name = "id", description = "Person Id") },
                        requestBody = @RequestBody(description = "New Person", content = @Content(schema = @Schema(implementation = Person.class))),
                        responses = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Person.class))),
                                @ApiResponse(responseCode = "404", description = "Person not found") }))
    public RouterFunction<ServerResponse> routeModifyPerson(PersonHandler personHandler) {
        return RouterFunctions.route(PUT("/people/{id}").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON)), personHandler::put);
    }

    @Bean
    @RouterOperation(operation = @Operation(operationId = "deletePerson", summary = "Delete a person by ID", tags = { "Person" },
                        parameters = { @Parameter(in = ParameterIn.PATH, name = "id", description = "Person Id") },
                        responses = { @ApiResponse(responseCode = "204", description = "successful operation"),
                                @ApiResponse(responseCode = "404", description = "Person not found") }))
    public RouterFunction<ServerResponse> routeDeletePerson(PersonHandler personHandler) {
        return RouterFunctions.route(DELETE("/people/{id}"), personHandler::delete);
    }
}
