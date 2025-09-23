package br.com.jzbreno.controllers;

import br.com.jzbreno.model.Person;
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

    public PersonController(PersonServices personServices) {
        this.personServices = personServices;
    }

    //adicionado informacoes no request
    @GetMapping(value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> findById(@PathVariable(name = "id") String id){
        Person person = personServices.findById(id);
        if(person != null) return ResponseEntity.ok().body(person);
        else return ResponseEntity.notFound().build();
    }

    //adicionado informacoes no request
    @GetMapping(value = "/findAll",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Person>> findAll(){
        List<Person> people = personServices.findAll();
        if (people.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(people);
    }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> create(@RequestBody Person person){
        return ResponseEntity.ok().body(personServices.create(person));
    }

    @PutMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> update(@RequestBody Person person){
        return ResponseEntity.ok().body(personServices.updating(person));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "id") String id){
        personServices.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
