package br.com.jzbreno.controllers;

import br.com.jzbreno.model.DTO.PersonDTO2;
import br.com.jzbreno.services.PersonServiceV2;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/person/v2")
@Tag(name = "API REST Person Version 2", description = "Enpoint for managing Persons, version 2 with Content Negotiation and Others tecnologies")
public class PersonControllerV2 implements PersonControllerV2Doc {

    private final PersonServiceV2 personServices;

    public PersonControllerV2(PersonServiceV2 personServices) {
        this.personServices = personServices;
    }

    //adicionado informacoes no request
    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<EntityModel<PersonDTO2>> findByIdV2(@PathVariable(name = "id") String id){
        EntityModel<PersonDTO2> person = personServices.findByIdV2(id);
        if(person != null) return ResponseEntity.ok().body(person);
        else return ResponseEntity.notFound().build();
    }

    //adicionado informacoes no request
    @GetMapping(value = "/findAll",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<PersonDTO2>>> findAllV2(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                         @RequestParam(name = "size", defaultValue = "15") Integer size,
                                                                         @RequestParam(name = "direction", defaultValue = "asc") String direction,
                                                                         @RequestParam(name = "properties", defaultValue = "firstName") String properties){

        Sort.Direction sortDirection = "desc".equalsIgnoreCase(properties) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageableValue = PageRequest.of(page, size, Sort.by(sortDirection, properties));
        PagedModel<EntityModel<PersonDTO2>> people = personServices.findAllV2(pageableValue);

        if (people.getContent().isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(people);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<EntityModel<PersonDTO2>> createV2(@RequestBody PersonDTO2 person){
        EntityModel<PersonDTO2> createdPerson = personServices.createV2(person);
        return ResponseEntity.ok().body(createdPerson);
    }

    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PersonDTO2> update(@RequestBody PersonDTO2 person){
        return ResponseEntity.ok().body(personServices.updating(person));
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") String id){
        personServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public void teste(){
        log.debug("Testando o logback");
    }

}
