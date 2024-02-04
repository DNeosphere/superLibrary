package com.unir.persons.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.unir.persons.model.pojo.PersonDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unir.persons.model.pojo.Person;
import com.unir.persons.model.request.CreatePersonRequest;
import com.unir.persons.service.PersonsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Persons Controller", description = "Microservicio encargado de exponer operaciones CRUD sobre personas alojadas en una base de datos en memoria.")
public class PersonsController {

    private final PersonsService service;

    @GetMapping("/persons")
    @Operation(
            operationId = "Obtener personas",
            description = "Operacion de lectura",
            summary = "Se devuelve una lista de todas las personas almacenadas en la base de datos.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class)))
    @ApiResponse(
            responseCode = "204",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    public ResponseEntity<List<Person>> getPersons(
            @RequestHeader Map<String, String> headers,
            @Parameter(name = "name", description = "Nombre de la persona. No tiene por que ser exacto", example = "El señor de los anillos", required = false)
            @RequestParam(required = false) String name,
            @Parameter(name = "lastname", description = "Apellido de la persona", example = "Pepito Perez", required = false)
            @RequestParam(required = false) String lastname,
            @Parameter(name = "visible", description = "Estado del libro. true o false", example = "true", required = false)
            @RequestParam(required = false) Boolean visible) {

        log.info("headers: {}", headers);
        List<Person> persons = service.getPersons(name, lastname,visible);

        if (persons != null) {
            return ResponseEntity.ok(persons);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/persons/{personId}")
    @Operation(
            operationId = "Obtener una persona",
            description = "Operacion de lectura",
            summary = "Se devuelve una persona a partir de su identificador.")
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado la persona con el identificador indicado.")
    public ResponseEntity<Person> getPerson(@PathVariable String personId) {

        log.info("Request received for person {}", personId);
        Person person = service.getPerson(personId);

        if (person != null) {
            return ResponseEntity.ok(person);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/persons/{personId}")
    @Operation(
            operationId = "Eliminar una persona",
            description = "Operacion de escritura",
            summary = "Se elimina una persona a partir de su identificador.")
    @ApiResponse(
            responseCode = "204",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado la persona con el identificador indicado.")
    public ResponseEntity<Void> deletePerson(@PathVariable String personId) {

        Boolean removed = service.removePerson(personId);

        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/persons")
    @Operation(
            operationId = "Insertar una persona",
            description = "Operacion de escritura",
            summary = "Se crea una persona a partir de sus datos.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la persona a crear.",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreatePersonRequest.class))))
    @ApiResponse(
            responseCode = "201",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Datos incorrectos introducidos.")
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "No se ha encontrado la persona con el identificador indicado.")
    public ResponseEntity<Person> addPerson(@RequestBody CreatePersonRequest request) {

        Person createdPerson = service.createPerson(request);

        if (createdPerson != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PatchMapping("/persons/{personId}")
    @Operation(
            operationId = "Modificar parcialmente una persona",
            description = "RFC 7386. Operacion de escritura",
            summary = "RFC 7386. Se modifica parcialmente una persona.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la persona a crear.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = String.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class)))
    @ApiResponse(
            responseCode = "400",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Persona invalida o datos incorrectos introducidos.")
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Persona no encontrada.")
    public ResponseEntity<Person> patchPerson(@PathVariable String personId, @RequestBody String patchBody) {

        Person patched = service.updatePerson(personId, patchBody);
        if (patched != null) {
            return ResponseEntity.ok(patched);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/persons/{personId}")
    @Operation(
            operationId = "Modificar totalmente una persona",
            description = "Operacion de escritura",
            summary = "Se modifica totalmente una persona.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la persona a actualizar.",
                    required = true,
                    content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = PersonDto.class))))
    @ApiResponse(
            responseCode = "200",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Person.class)))
    @ApiResponse(
            responseCode = "404",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)),
            description = "Persona no encontrada.")
    public ResponseEntity<Person> updatePerson(@PathVariable String personId, @RequestBody PersonDto body) {

        Person updated = service.updatePerson(personId, body);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
