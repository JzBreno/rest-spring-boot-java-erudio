package br.com.jzbreno.controllers;

import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.DTO.PersonDTO2;
import br.com.jzbreno.services.PersonServiceV2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// sda
@RestController
@RequestMapping("/person/v2")
public class PersonControllerV2 {

    private final PersonServiceV2 personServices;

    public PersonControllerV2(PersonServiceV2 personServices) {
        this.personServices = personServices;
    }

    //adicionado informacoes no request
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO2> findByIdV2(@PathVariable(name = "id") String id){
        PersonDTO2 person = personServices.findByIdV2(id);
        if(person != null) return ResponseEntity.ok().body(person);
        else return ResponseEntity.notFound().build();
    }

    //adicionado informacoes no request
    @GetMapping(value = "/findAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonDTO2>> findAllV2(){
        List<PersonDTO2> people = personServices.findAllV2();
        if (people.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(people);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO2> createV2(@RequestBody PersonDTO2 person){
        return ResponseEntity.ok().body(personServices.createV2(person));
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonDTO2> update(@RequestBody PersonDTO person){
        return ResponseEntity.ok().body(personServices.updating(person));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") String id){
        personServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
