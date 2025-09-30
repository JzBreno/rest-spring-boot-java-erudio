package br.com.jzbreno.controllers;

import br.com.jzbreno.mapper.PersonMapper;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.DTO.PersonDTO2;
import br.com.jzbreno.services.PersonServices;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController{

//    @Autowired
//    private PersonServices personServicesAutowired;
//    assim seria a forma de injecao com anotacao, mas irei usar no construtor como boa pratica

    private final PersonServices personServices;
    private final PersonMapper personMapper;

    public PersonController(PersonServices personServices, PersonMapper personMapper) {
        this.personServices = personServices;
        this.personMapper = personMapper;
    }

    //adicionado informacoes no request
    @GetMapping(value = "/v1/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> findById(@PathVariable(name = "id") String id){
        PersonDTO person = personServices.findById(id);
        if(person != null) return ResponseEntity.ok().body(person);
        else return ResponseEntity.notFound().build();
    }

    //adicionado informacoes no request
    @GetMapping(value = "/v2/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO2> findByIdV2(@PathVariable(name = "id") String id){
        PersonDTO2 person = personServices.findByIdV2(id);
        if(person != null) return ResponseEntity.ok().body(person);
        else return ResponseEntity.notFound().build();
    }

    //adicionado informacoes no request
    @GetMapping(value = "/v1/findAll",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonDTO>> findAll(){
        List<PersonDTO> people = personServices.findAll();
        if (people.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(people);
    }

    //adicionado informacoes no request
    @GetMapping(value = "/v2/findAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonDTO2>> findAllV2(){
        List<PersonDTO2> people = personServices.findAllV2();
        if (people.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(people);
    }

    @PostMapping(value = "/v1",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> createV1(@RequestBody PersonDTO person){
        return ResponseEntity.ok().body(personServices.createV1(person));
    }

    @PostMapping(value = "/v2",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO2> createV2(@RequestBody PersonDTO2 person){
        return ResponseEntity.ok().body(personServices.createV2(person));
    }

    @PutMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO> update(@RequestBody PersonDTO person){
        return ResponseEntity.ok().body(personServices.updating(person));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") String id){
        personServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
