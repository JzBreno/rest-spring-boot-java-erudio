package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.ResourceNotFoundException;
import br.com.jzbreno.model.Person;
import br.com.jzbreno.repository.PersonRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

@Service
//essa anotacao serve para deixar claro que e uma classe de logica de negocio, e podemos injetala onde precisamos, com  @Autowired ou injecao por construtor(mais recomendado)
//alias para o anotation component

public class PersonServices {

//    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());
    private final PersonRepository personRepository;

    public PersonServices(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findById(String id){
        logger.info("Finding person by id");
        return personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("Person not found for this id :: " + id));
    }

    public List<Person> findAll(){
        logger.info("Finding all people");
        return personRepository.findAll();
    }

    public Person create(@NonNull Person person){
        logger.info("Creating person");
        personRepository.save(person);
        return person;
    }

    public Person updating(Person person){
        logger.info("Updating person");
        Person personUpdate = findById(person.getId().toString());
        personUpdate.setFirstName(person.getFirstName());
        personUpdate.setLastName(person.getLastName());
        personUpdate.setGender(person.getGender());
        return personRepository.save(personUpdate);
    }

    public void deleteById(String id){
        logger.info("Deleting person");
        personRepository.deleteById(Long.parseLong(id));
    }

}
