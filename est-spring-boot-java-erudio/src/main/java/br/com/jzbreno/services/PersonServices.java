package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.RequiredObjectIsNullException;
import br.com.jzbreno.Exceptions.ResourceNotFoundException;
import br.com.jzbreno.controllers.PersonController;
import br.com.jzbreno.mapper.ObjectMapper;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.Person;
import br.com.jzbreno.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //Pageable e a interface que recebe indice de busca, tamanho da busca e ordencacao, ainda retorna alguns metadados importantes
    // como qtd de registros etc
    public Page<PersonDTO> findAll(Pageable pageable){
        log.info("Finding all people");
        log.info("list of people : " + personRepository.findAll().toString());

        Page<Person> people = personRepository.findAll(pageable);

        Page<PersonDTO> peopleWithLinks = people.map(person -> {
            var dto = ObjectMapper.parseObject(person, PersonDTO.class);
            implementsHateoasPerson(dto);
            return dto;
        });


        return peopleWithLinks;
    }



    public PersonDTO createV1(@NonNull PersonDTO person){

        if(person == null) throw new RequiredObjectIsNullException();

        log.info("Creating person : " + person.toString());
        personRepository.save(ObjectMapper.parseObject(person, Person.class));
        implementsHateoasPerson(person);
        return person;
    }



    public PersonDTO updating(PersonDTO person){

        if(person == null) throw new RequiredObjectIsNullException();

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
    //com essa anotacao, garante que os beans que usam de jpa query(fora do padrao do jpa) language cumpram com todos os requisitos de transacao
    @Transactional
    public PersonDTO disablePersonId(String id){


        log.info("disable person : " + id);
        personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("PersonDTO not found for this id :: " + id));
        personRepository.disablePerson(Long.parseLong(id));
        //cache de egundo nivelhibernate
        //quando fazemos consultas duplicadas no hibernate ele pega do cache e nao faz uma consulta no banco novamente
        return findById(id);

    }

    public void deleteById(String id){
        log.info("Deleting person : " + id);
        personRepository.deleteById(Long.parseLong(id));
        implementsHateoasPerson(findById(id));
    }

    private static void implementsHateoasPerson(PersonDTO personDTO) {
        personDTO.add(linkTo(methodOn(PersonController.class).findById(String.valueOf(personDTO.getId()))).withSelfRel().withType("GET"));
        personDTO.add(linkTo(methodOn(PersonController.class).findAll(1, 15)).withRel("findAll").withType("GET"));
        personDTO.add(linkTo(methodOn(PersonController.class).deleteById(String.valueOf(personDTO.getId()))).withRel("deleteById").withType("DELETE"));
        personDTO.add(linkTo(methodOn(PersonController.class).createV1(personDTO)).withRel("createV1").withType("POST"));
        personDTO.add(linkTo(methodOn(PersonController.class).update(personDTO)).withRel("update").withType("PUT"));
        personDTO.add(linkTo(methodOn(PersonController.class).disablePersonById(String.valueOf(personDTO.getId()))).withRel("disablePersonId").withType("PATCH"));

    }

    private static void implementsHateoasPerson(List<PersonDTO> personDTOList) {
        for (PersonDTO personDTO : personDTOList) {
            personDTO.add(linkTo(methodOn(PersonController.class).findById(String.valueOf(personDTO.getId()))).withSelfRel().withType("GET"));
            personDTO.add(linkTo(methodOn(PersonController.class).findAll(1, 15)).withRel("findAll").withType("GET"));
            personDTO.add(linkTo(methodOn(PersonController.class).deleteById(String.valueOf(personDTO.getId()))).withRel("deleteById").withType("DELETE"));
            personDTO.add(linkTo(methodOn(PersonController.class).createV1(personDTO)).withRel("createV1").withType("POST"));
            personDTO.add(linkTo(methodOn(PersonController.class).update(personDTO)).withRel("update").withType("PUT"));
            personDTO.add(linkTo(methodOn(PersonController.class).disablePersonById(String.valueOf(personDTO.getId()))).withRel("disablePersonId").withType("PATCH"));
        }

    }

}
