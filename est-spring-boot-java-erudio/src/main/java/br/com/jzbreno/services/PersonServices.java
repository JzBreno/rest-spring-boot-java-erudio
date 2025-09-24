package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.ResourceNotFoundException;
import br.com.jzbreno.model.Person;
import br.com.jzbreno.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//essa anotacao serve para deixar claro que e uma classe de logica de negocio, e podemos injetala onde precisamos, com  @Autowired ou injecao por construtor(mais recomendado)
//alias para o anotation component
@Slf4j
public class PersonServices {

//    private final AtomicLong counter = new AtomicLong();
//    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());
    private final PersonRepository personRepository;

    public PersonServices(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findById(String id){
        log.info("Finding person by id : " + id);
        return personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("Person not found for this id :: " + id));
    }

    public List<Person> findAll(){
        log.info("Finding all people");
        log.info("list of people : " + personRepository.findAll().toString());
        return personRepository.findAll();
    }

    public Person create(@NonNull Person person){
        log.info("Creating person : " + person.toString());
        personRepository.save(person);
        return person;
    }

    public Person updating(Person person){
        log.info("Updating person : " + person.toString() );
        Person personUpdate = findById(person.getId().toString());
        personUpdate.setFirstName(person.getFirstName());
        personUpdate.setLastName(person.getLastName());
        personUpdate.setGender(person.getGender());
        return personRepository.save(personUpdate);
    }

    public void deleteById(String id){
        log.info("Deleting person : " + id);
        personRepository.deleteById(Long.parseLong(id));
    }

}
