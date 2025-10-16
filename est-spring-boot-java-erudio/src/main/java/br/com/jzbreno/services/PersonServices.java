package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.ResourceNotFoundException;
import br.com.jzbreno.controllers.PersonController;
import br.com.jzbreno.mapper.ObjectMapper;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.Person;
import br.com.jzbreno.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    public PersonDTO findById(String id){
        log.info("Finding person by id : " + id);
        PersonDTO personDTO = ObjectMapper.parseObject(personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("PersonDTO not found for this id :: " + id)), PersonDTO.class);
        implementsHateoasPerson(personDTO);
        return personDTO;
    }

    public List<PersonDTO> findAll(){
        log.info("Finding all people");
        log.info("list of people : " + personRepository.findAll().toString());
        List<PersonDTO> listaDto = ObjectMapper.parseObjectList(personRepository.findAll(), PersonDTO.class);
        implementsHateoasPerson(listaDto);
        return listaDto;
    }



    public PersonDTO createV1(@NonNull PersonDTO person){
        log.info("Creating person : " + person.toString());
        personRepository.save(ObjectMapper.parseObject(person, Person.class));
        implementsHateoasPerson(person);
        return person;
    }



    public PersonDTO updating(PersonDTO person){
        log.info("Updating person : " + person.toString() );
        PersonDTO personUpdate = findById(person.getId().toString());
        personUpdate.setFirstName(person.getFirstName());
        personUpdate.setLastName(person.getLastName());
        personUpdate.setGender(person.getGender());
        Person personVo = ObjectMapper.parseObject(person, Person.class);
        personRepository.save(personVo);
        implementsHateoasPerson(personUpdate);
        return personUpdate;
    }

    public void deleteById(String id){
        log.info("Deleting person : " + id);
        personRepository.deleteById(Long.parseLong(id));
        implementsHateoasPerson(findById(id));
    }

    private static void implementsHateoasPerson(PersonDTO personDTO) {
        personDTO.add(linkTo(methodOn(PersonController.class).findById(String.valueOf(personDTO.getId()))).withSelfRel().withType("GET"));
        personDTO.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
        personDTO.add(linkTo(methodOn(PersonController.class).deleteById(String.valueOf(personDTO.getId()))).withRel("deleteById").withType("DELETE"));
        personDTO.add(linkTo(methodOn(PersonController.class).createV1(personDTO)).withRel("createV1").withType("POST"));
        personDTO.add(linkTo(methodOn(PersonController.class).update(personDTO)).withRel("update").withType("PUT"));

    }

    private static void implementsHateoasPerson(List<PersonDTO> personDTOList) {
        for (PersonDTO personDTO : personDTOList) {
            personDTO.add(linkTo(methodOn(PersonController.class).findById(String.valueOf(personDTO.getId()))).withSelfRel().withType("GET"));
            personDTO.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
            personDTO.add(linkTo(methodOn(PersonController.class).deleteById(String.valueOf(personDTO.getId()))).withRel("deleteById").withType("DELETE"));
            personDTO.add(linkTo(methodOn(PersonController.class).createV1(personDTO)).withRel("createV1").withType("POST"));
            personDTO.add(linkTo(methodOn(PersonController.class).update(personDTO)).withRel("update").withType("PUT"));
        }

    }

}
