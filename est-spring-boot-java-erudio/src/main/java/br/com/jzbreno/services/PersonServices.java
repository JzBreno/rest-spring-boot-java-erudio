package br.com.jzbreno.services;

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
    List<Person> people = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());
    private final PersonRepository personRepository;

    public PersonServices(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findById(String id){
        logger.info("Finding person by id");
        return personRepository.findById(Long.parseLong(id)).isPresent() ? personRepository.findById(Long.parseLong(id)).get() : null;
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
        people.removeIf(p -> p.getId().equals(person.getId()));
        people.add(person);
        logger.info("Updating person");
        Optional<Person> person1  = personRepository.findById(1L);
        personRepository.delete(person1.get());
        return person;
    }

    public void deleteById(String id){
        logger.info("Deleting person");
        personRepository.deleteById(Long.parseLong(id));
    }

}
