package com.unir.operator.controller;

import java.util.Collections;
import java.util.List;
import com.unir.operator.model.pojo.BorrowDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unir.operator.model.pojo.Borrow;
import com.unir.operator.model.request.CreateBorrowRequest;
import com.unir.operator.service.BorrowsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Operator Controller", description = "Microservicio encargado de exponer operaciones sobre los libros alojados en una base de datos en memoria.")
public class OperatorController {

    private final BorrowsService service;

    @GetMapping("/borrows/person/{personId}")
    @Operation(operationId = "Obtener prestamos de una persona", description = "Operacion de lectura", summary = "Se consultan los prestamos a partir de un identificador de persona.")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Borrow.class)))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)), description = "No se han encontrado los prestamos con el identificador indicado.")
    public ResponseEntity<List<Borrow>> findBorrowsByPerson(@PathVariable Long personId) {
        List<Borrow> borrows = service.findBorrowsByPerson(personId);
        if (borrows != null) {
            return ResponseEntity.ok(borrows);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/borrows/person/penalties/{personId}")
    @Operation(operationId = "Obtener prestamos penalizados de una persona", description = "Operacion de lectura", summary = "Se consultan los prestamos penalizados a partir de un identificador de persona.")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Borrow.class)))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)), description = "No se han encontrado los prestamos penalizados con el identificador indicado.")
    public ResponseEntity<List<Borrow>> findBorrowsPenaltiesByPerson(@PathVariable Long personId) {
        List<Borrow> borrows = service.findBorrowsPenaltiesByPerson(personId);
        if (borrows != null) {
            return ResponseEntity.ok(borrows);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/borrows/{borrowId}")
    @Operation(operationId = "Obtener un prestamo", description = "Operacion de lectura", summary = "Se devuelve un prestamo a partir de su identificador.")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Borrow.class)))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)), description = "No se ha encontrado el prestamo con el identificador indicado.")
    public ResponseEntity<Borrow> getBorrow(@PathVariable Long borrowId) {

        log.info("Request received for borrow {}", borrowId);
        Borrow borrow = service.getBorrow(borrowId);

        if (borrow != null) {
            return ResponseEntity.ok(borrow);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/borrows/{borrowId}")
    @Operation(operationId = "Eliminar un prestamo", description = "Operacion de escritura", summary = "Se elimina un prestamo a partir de su identificador.")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)), description = "No se ha encontrado el prestamo con el identificador indicado.")
    public ResponseEntity<Void> deleteBorrow(@PathVariable Long borrowId) {

        Boolean removed = service.removeBorrow(borrowId);

        if (Boolean.TRUE.equals(removed)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/borrows")
    @Operation(operationId = "Insertar un prestamo", description = "Operacion de escritura", summary = "Se crea un prestamo a partir de sus datos.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del prestamo a crear.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateBorrowRequest.class))))
    @ApiResponse(responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Borrow.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)), description = "Datos incorrectos introducidos.")
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)), description = "No se ha encontrado el libro con el identificador indicado.")
    public ResponseEntity<Borrow> addBorrow(@RequestBody CreateBorrowRequest request) {

        Borrow createdBorrow = service.createBorrow(request);

        if (createdBorrow != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBorrow);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/borrows/turnin/{borrowId}")
    @Operation(operationId = "Devolver un libro por parte de la persona", description = "Operacion de escritura", summary = "Se devuelve un libro por parte de una persona", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del prestamo a actualizar.", required = true, content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = BorrowDto.class))))
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Borrow.class)))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)), description = "Prestamo no encontrado.")
    public ResponseEntity<Borrow> turnInBorrow(@PathVariable Long borrowId) {

        Borrow updated = service.turnIn(Long.valueOf(borrowId));
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/borrows/{borrowId}")
    @Operation(operationId = "Modificar totalmente un prestamo de libro", description = "Operacion de escritura", summary = "Se modifica totalmente un prestamo de libro.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del prestamo a actualizar.", required = true, content = @Content(mediaType = "application/merge-patch+json", schema = @Schema(implementation = BorrowDto.class))))
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Borrow.class)))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)), description = "Prestamo no encontrado.")
    public ResponseEntity<Borrow> updateBorrow(@PathVariable Long borrowId, @RequestBody BorrowDto body) {

        Borrow updated = service.updateBorrow(Long.valueOf(borrowId), body);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}