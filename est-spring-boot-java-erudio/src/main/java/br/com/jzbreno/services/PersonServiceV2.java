package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.ResourceNotFoundException;
import br.com.jzbreno.controllers.PersonControllerV2;
import br.com.jzbreno.mapper.ObjectMapper;
import br.com.jzbreno.mapper.PersonMapper;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.DTO.PersonDTO2;
import br.com.jzbreno.model.Person;
import br.com.jzbreno.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Slf4j
public class PersonServiceV2 {

    private final PersonRepository personRepository;
    private PersonMapper personMapper = new PersonMapper();
//    necessario para mappear o objeto com links HAL
    @Autowired
    private PagedResourcesAssembler<PersonDTO2> pagedResourcesAssembler;

    public PersonServiceV2(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public EntityModel<PersonDTO2> findByIdV2(String id){
        log.info("Finding person by id : " + id);
        PersonDTO2 personDTO2 = personMapper.parsePersonDTOV2(personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("PersonDTO not found for this id :: " + id)));
        implementsHateoasPerson(personDTO2);
        //adicionando HAL
        Link selfLink = linkTo(
                methodOn(PersonControllerV2.class).findByIdV2(id)
        ).withSelfRel();
        Link deleteLink = linkTo(
                methodOn(PersonControllerV2.class).deleteById(id)
        ).withSelfRel().withType("delete");

        return EntityModel.of(personDTO2)
                .add(selfLink)
                .add(deleteLink);
//        return ObjectMapper.parseObject(personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("PersonDTO not found for this id :: " + id)), PersonDTO2.class);
    }

    public PagedModel<EntityModel<PersonDTO2>> findAllV2(Pageable pageable){
        log.info("Finding all people");
        log.info("list of people : " + personRepository.findAll().toString());
        Page<PersonDTO2> pagePersonDto2 = personRepository.findAll(pageable).map(person -> personMapper.parsePersonDTOV2(person));

        Page<PersonDTO2> peopleWithLinks = pagePersonDto2.map(person -> {
            var dto = ObjectMapper.parseObject(person, PersonDTO2.class);
            implementsHateoasPerson(dto);
            return dto;
        });

        //adicionado HAL
        Link findAllLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PersonControllerV2.class)
                        .findAllV2(pageable.getPageNumber(),
                                pageable.getPageSize(),
                                String.valueOf(pageable.getSort()),
                                "firstName")
        ).withSelfRel();


        return pagedResourcesAssembler.toModel(peopleWithLinks, findAllLink);
    }

    public EntityModel<PersonDTO2> createV2(@NonNull PersonDTO2 personDTO){ // Renomeei para personDTO para clareza
        log.info("Creating person : " + personDTO.toString());

        // 1. Converte DTO -> Entity
        Person entity = personMapper.parseDT0V2Person(personDTO);

        // 2. Salva e CAPTURA o retorno (aqui está o ID gerado!)
        Person savedEntity = personRepository.save(entity);

        // 3. Atualiza o DTO com o ID que veio do banco
        personDTO.setId(savedEntity.getId());

        // 4. Gera os links
        implementsHateoasPerson(personDTO);
        List<Link> selfLink = generateHAL(personDTO);

        return EntityModel.of(personDTO, selfLink);
    }

    private List<Link> generateHAL(PersonDTO2 person) {
        List<Link> links = new ArrayList<>();
        Link self = linkTo(
                methodOn(PersonControllerV2.class).createV2(person)
        ).withSelfRel().withType("create");
        Link findById = linkTo(
                methodOn(PersonControllerV2.class).findByIdV2(person.getId().toString())
        ).withRel("findById");
        Link findAll = linkTo(
                methodOn(PersonControllerV2.class).findAllV2(0, 1, "asc", "FirstName" )
        ).withRel("findAll");
        Link delete = linkTo(
                methodOn(PersonControllerV2.class).deleteById(person.getId().toString())
        ).withRel("delete");
        Link update = linkTo(
                methodOn(PersonControllerV2.class).update(person)
        ).withRel("update");

        links.add(self);
        links.add(findById);
        links.add(findAll);
        links.add(delete);
        links.add(update);
        return links;
    }

    public PersonDTO2 updating(PersonDTO2 person){
        log.info("Updating person : " + person.toString() );
        PersonDTO2 personUpdate = findByIdV2(person.getId().toString()).getContent();
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
