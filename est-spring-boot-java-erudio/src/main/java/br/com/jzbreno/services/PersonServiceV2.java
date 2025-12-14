package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.ResourceNotFoundException;
import br.com.jzbreno.controllers.PersonController;
import br.com.jzbreno.controllers.PersonControllerV2;
import br.com.jzbreno.mapper.ObjectMapper;
import br.com.jzbreno.mapper.PersonMapper;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.DTO.PersonDTO2;
import br.com.jzbreno.model.Person;
import br.com.jzbreno.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Slf4j
public class PersonServiceV2 {

    private final PersonRepository personRepository;
    private PersonMapper personMapper = new PersonMapper();

//dasda
    public PersonServiceV2(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonDTO2 findByIdV2(String id){
        log.info("Finding person by id : " + id);
        PersonDTO2 personDTO2 = personMapper.parsePersonDTOV2(personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("PersonDTO not found for this id :: " + id)));
        implementsHateoasPerson(personDTO2);
        return personDTO2;
//        return ObjectMapper.parseObject(personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("PersonDTO not found for this id :: " + id)), PersonDTO2.class);
    }

    public Page<PersonDTO2> findAllV2(Pageable pageable){
        log.info("Finding all people");
        log.info("list of people : " + personRepository.findAll().toString());
        Page<PersonDTO2> pagePersonDto2 = personRepository.findAll(pageable).map(person -> personMapper.parsePersonDTOV2(person));
        pagePersonDto2.stream().map( person -> {
            var dto = ObjectMapper.parseObject( person, PersonDTO2.class);
            implementsHateoasPerson(dto);
            return dto;
        });

        return pagePersonDto2;
    }

    public PersonDTO2 createV2(@NonNull PersonDTO2 person){
        log.info("Creating person : " + person.toString());
        personRepository.save(personMapper.parseDT0V2Person(person));
        implementsHateoasPerson(person);
        return person;
    }

    public PersonDTO2 updating(PersonDTO2 person){
        log.info("Updating person : " + person.toString() );
        PersonDTO2 personUpdate = findByIdV2(person.getId().toString());
        personUpdate.setFirstName(person.getFirstName());
        personUpdate.setLastName(person.getLastName());
        personUpdate.setGender(person.getGender());
        personUpdate.setAddress(person.getAddress());
        personUpdate.setBirthday(person.getBirthday());
        Person personVo = personMapper.parseDT0V2Person(personUpdate);
        personRepository.save(personVo);
        implementsHateoasPerson(personUpdate);
        return personUpdate;
    }

    public void deleteById(String id){
        log.info("Deleting person : " + id);
        personRepository.deleteById(Long.parseLong(id));
        implementsHateoasPerson(findByIdV2(id));
    }

    private static void implementsHateoasPerson(PersonDTO2 personDTO) {
        personDTO.add(linkTo(methodOn(PersonControllerV2.class).findByIdV2(String.valueOf(personDTO.getId()))).withSelfRel().withType("GET"));
        personDTO.add(linkTo(methodOn(PersonControllerV2.class).findAllV2(0, 15, "asc", "firstName")).withRel("findAll").withType("GET"));
        personDTO.add(linkTo(methodOn(PersonControllerV2.class).deleteById(String.valueOf(personDTO.getId()))).withRel("deleteById").withType("DELETE"));
        personDTO.add(linkTo(methodOn(PersonControllerV2.class).createV2(personDTO)).withRel("createV1").withType("POST"));
        personDTO.add(linkTo(methodOn(PersonControllerV2.class).update(personDTO)).withRel("update").withType("PUT"));

    }

    private static void implementsHateoasPerson(List<PersonDTO2> personDTOList) {
        for (PersonDTO2 personDTO : personDTOList) {
            personDTO.add(linkTo(methodOn(PersonControllerV2.class).findByIdV2(String.valueOf(personDTO.getId()))).withSelfRel().withType("GET"));
            personDTO.add(linkTo(methodOn(PersonControllerV2.class).findAllV2(0, 15, "asc", "firstName")).withRel("findAll").withType("GET"));
            personDTO.add(linkTo(methodOn(PersonControllerV2.class).deleteById(String.valueOf(personDTO.getId()))).withRel("deleteById").withType("DELETE"));
            personDTO.add(linkTo(methodOn(PersonControllerV2.class).createV2(personDTO)).withRel("createV1").withType("POST"));
            personDTO.add(linkTo(methodOn(PersonControllerV2.class).update(personDTO)).withRel("update").withType("PUT"));
        }

    }

}
