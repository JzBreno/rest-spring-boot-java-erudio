package br.com.jzbreno.services;

import br.com.jzbreno.Exceptions.FileStorageException;
import br.com.jzbreno.Exceptions.RequiredObjectIsNullException;
import br.com.jzbreno.Exceptions.ResourceNotFoundException;
import br.com.jzbreno.controllers.PersonController;
import br.com.jzbreno.file.importer.contract.FileImporter;
import br.com.jzbreno.file.importer.factory.FileImporterFactory;
import br.com.jzbreno.mapper.ObjectMapper;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.Person;
import br.com.jzbreno.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    PagedResourcesAssembler<PersonDTO> pagedResourcesAssembler;

    @Autowired
    FileImporterFactory importer;

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
    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable){
        log.info("Finding all people");
        log.info("list of people : " + personRepository.findAll().toString());

        Page<Person> people = personRepository.findAll(pageable);

        Page<PersonDTO> peopleWithLinks = people.map(person -> {
            var dto = ObjectMapper.parseObject(person, PersonDTO.class);
            implementsHateoasPerson(dto);
            return dto;
        });

        Link findAllLink = getAllLinks(pageable);

        return pagedResourcesAssembler.toModel(peopleWithLinks, findAllLink);
    }

    public PagedModel<EntityModel<PersonDTO>> findPersonByName(String firstName, Pageable pageable){
        log.info("Finding all people");
        log.info("list of people : " + personRepository.findPersonByFirstName(firstName, pageable).toString());

        Page<Person> people = personRepository.findPersonByFirstName(firstName, pageable);

        Page<PersonDTO> peopleWithLinks = people.map(person -> {
            var dto = ObjectMapper.parseObject(person, PersonDTO.class);
            implementsHateoasPerson(dto);
            return dto;
        });

        Link findAllLink = getAllLinks(pageable);

        return pagedResourcesAssembler.toModel(peopleWithLinks, findAllLink);
    }



    public PersonDTO createV1(@NonNull PersonDTO person){

        if(person == null) throw new RequiredObjectIsNullException();

        log.info("Creating person : " + person.toString());
        personRepository.save(ObjectMapper.parseObject(person, Person.class));
        implementsHateoasPerson(person);
        return person;
    }
    //endpoint para inserir apartir de csv e xls
    public List<PersonDTO> massCreation(MultipartFile file){
        log.info("Mass creation of people");
        if(file.isEmpty()) throw new RequiredObjectIsNullException();

        try(InputStream inputStream = file.getInputStream()){
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name cannont be null"));
            //ele busca o tipo de instancia de FileImporter que e o tipo de arquivo que e enviado, e retorna uma instancia de FileImporter
            FileImporter importer = this.importer.getFileImporter(fileName);
            //cria as entidades e utiliza o .stream para percorrer e modificar com o .map e ja salvar no banco chamando o repository e retorna uma lista
            List<Person> entities = importer.importFile(inputStream)
                    .stream().map(dto -> personRepository.save(ObjectMapper.parseObject(dto, Person.class)))
                    .toList();
            //usando pipeline para retornar a lista de person como lista de DTO
            return entities.stream()
                    .map(entitie -> {
                        var  dto = ObjectMapper.parseObject(entitie, PersonDTO.class);
                        implementsHateoasPerson(dto);
                        return dto;
                    }).toList();

        } catch (Exception e) {
            throw new FileStorageException("Error while creating people", e);
        }

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
        personDTO.add(linkTo(methodOn(PersonController.class).findAll(1, 15, "asc", "firstName")).withRel("findAll").withType("GET"));
        personDTO.add(linkTo(methodOn(PersonController.class).deleteById(String.valueOf(personDTO.getId()))).withRel("deleteById").withType("DELETE"));
        personDTO.add(linkTo(methodOn(PersonController.class).createV1(personDTO)).withRel("createV1").withType("POST"));
        personDTO.add(linkTo(methodOn(PersonController.class)).slash("massCreate").withRel("MassCreate").withType("POST"));
        personDTO.add(linkTo(methodOn(PersonController.class).update(personDTO)).withRel("update").withType("PUT"));
        personDTO.add(linkTo(methodOn(PersonController.class).disablePersonById(String.valueOf(personDTO.getId()))).withRel("disablePersonId").withType("PATCH"));

    }

    private static void implementsHateoasPerson(List<PersonDTO> personDTOList) {
        for (PersonDTO personDTO : personDTOList) {
            personDTO.add(linkTo(methodOn(PersonController.class).findById(String.valueOf(personDTO.getId()))).withSelfRel().withType("GET"));
            personDTO.add(linkTo(methodOn(PersonController.class)).slash("massCreate").withRel("MassCreate").withType("POST"));
            personDTO.add(linkTo(methodOn(PersonController.class).findAll(1, 15,"asc","firstName")).withRel("findAll").withType("GET"));
            personDTO.add(linkTo(methodOn(PersonController.class).deleteById(String.valueOf(personDTO.getId()))).withRel("deleteById").withType("DELETE"));
            personDTO.add(linkTo(methodOn(PersonController.class).createV1(personDTO)).withRel("createV1").withType("POST"));
            personDTO.add(linkTo(methodOn(PersonController.class).update(personDTO)).withRel("update").withType("PUT"));
            personDTO.add(linkTo(methodOn(PersonController.class).disablePersonById(String.valueOf(personDTO.getId()))).withRel("disablePersonId").withType("PATCH"));
        }

    }

    private static Link getAllLinks(Pageable pageable) {
        Link findAllLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(PersonController.class)
                        .findAll(pageable.getPageNumber(),
                                pageable.getPageSize(),
                                String.valueOf(pageable.getSort()),
                                "firstName"
                        )).withSelfRel();
        return findAllLink;
    }
}
