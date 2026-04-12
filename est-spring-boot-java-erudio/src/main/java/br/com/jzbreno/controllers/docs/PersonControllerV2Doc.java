package br.com.jzbreno.controllers.docs;

import br.com.jzbreno.model.DTO.PersonDTO2;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface PersonControllerV2Doc {

    @Operation(summary = "Find a Person by ID", tags = {"People"})
    ResponseEntity<EntityModel<PersonDTO2>> findByIdV2(@PathVariable(name = "id") String id);

    @Operation(summary = "Find a list of All Person", tags = {"People"})
    ResponseEntity<PagedModel<EntityModel<PersonDTO2>>> findAllV2(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "15") Integer size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sort", defaultValue = "firstName") String properties);

    @Operation(summary = "Find persons by name", tags = {"People"})
    ResponseEntity<PagedModel<EntityModel<PersonDTO2>>> findPersonByName(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "15") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "properties", defaultValue = "firstName") String properties,
            @PathVariable(name = "firstName") String firstName); // Corrigido para firstName

    @Operation(summary = "Create a new Person", tags = {"People"})
    ResponseEntity<EntityModel<PersonDTO2>> createV2(@RequestBody PersonDTO2 person);

    @Operation(summary = "Update an existing Person", tags = {"People"})
    ResponseEntity<PersonDTO2> update(@RequestBody PersonDTO2 person);

    @Operation(summary = "Delete a Person by ID", tags = {"People"})
    ResponseEntity<Void> deleteById(@PathVariable(name = "id") String id);

    void teste();
}