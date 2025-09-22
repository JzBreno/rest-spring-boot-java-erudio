package br.com.jzbreno.services;

import br.com.jzbreno.model.Person;
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

    public Person findById(String id){
        logger.info("Finding person by id");
        Person person = createPerson();
        people.add(person);
        return person;
    }

    public List<Person> findAll(){
        logger.info("Finding all persons");

        if(people.isEmpty()){
            for (int i = 0; i < 10; i++) {
                people.add(createPerson());
            }
        }
        return people;
    }

    public Person create(String id){
        logger.info("Creating person");
        Person person = createPerson();
        return person;
    }

    public Person create(@NonNull Person person){
        person.setId(counter.incrementAndGet());
        logger.info("Creating person");
        people.add(person);
        return person;
    }

    public Person updating(Person person){
        people.removeIf(p -> p.getId().equals(person.getId()));
        people.add(person);
        logger.info("Updating person");
        return person;
    }

    public void deleteById(String id){
        Person person = people.get(Integer.parseInt(id));
        people.remove(person);
        logger.info("Deleting person by id" + id + " was successful" + person.toString());
        logger.info("Deleting person by id" + id + " was successful");
    }



    private Person createPerson(){
        logger.info("Creating person");
        Person person = new Person(counter.incrementAndGet(), "Jose", "Breno", "hender");
//        people.add(person);
        return person;
    }
}
