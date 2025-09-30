package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.ResourceNotFoundException;
import br.com.jzbreno.mapper.ObjectMapper;
import br.com.jzbreno.mapper.PersonMapper;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.DTO.PersonDTO2;
import br.com.jzbreno.model.Person;
import br.com.jzbreno.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
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
    private PersonMapper personMapper = new PersonMapper();
    public PersonServices(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonDTO findById(String id){
        log.info("Finding person by id : " + id);
        return ObjectMapper.parseObject(personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("PersonDTO not found for this id :: " + id)), PersonDTO.class);
    }

    public PersonDTO2 findByIdV2(String id){
        log.info("Finding person by id : " + id);
        return personMapper.parsePersonDTOV2(personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("PersonDTO not found for this id :: " + id)));
//        return ObjectMapper.parseObject(personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("PersonDTO not found for this id :: " + id)), PersonDTO2.class);
    }

    public List<PersonDTO> findAll(){
        log.info("Finding all people");
        log.info("list of people : " + personRepository.findAll().toString());
        return ObjectMapper.parseObjectList(personRepository.findAll(), PersonDTO.class);
    }

    public List<PersonDTO2> findAllV2(){
        log.info("Finding all people");
        log.info("list of people : " + personRepository.findAll().toString());
        return personMapper.parseListPersonDTOV2(personRepository.findAll()) ;
    }

    public PersonDTO createV1(@NonNull PersonDTO person){
        log.info("Creating person : " + person.toString());
        personRepository.save(ObjectMapper.parseObject(person, Person.class));
        return person;
    }

    public PersonDTO2 createV2(@NonNull PersonDTO2 person){
        log.info("Creating person : " + person.toString());
        personRepository.save(personMapper.parseDT0V2Person(person));
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
        return personUpdate;
    }

    public void deleteById(String id){
        log.info("Deleting person : " + id);
        personRepository.deleteById(Long.parseLong(id));
    }

}
