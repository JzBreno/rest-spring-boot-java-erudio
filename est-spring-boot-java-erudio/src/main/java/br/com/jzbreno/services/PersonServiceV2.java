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
        return personMapper.parsePersonDTOV2(personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("PersonDTO not found for this id :: " + id)));
//        return ObjectMapper.parseObject(personRepository.findById(Long.parseLong(id)).orElseThrow(() -> new ResourceNotFoundException("PersonDTO not found for this id :: " + id)), PersonDTO2.class);
    }

    public List<PersonDTO2> findAllV2(){
        log.info("Finding all people");
        log.info("list of people : " + personRepository.findAll().toString());
        return personMapper.parseListPersonDTOV2(personRepository.findAll()) ;
    }

    public PersonDTO2 createV2(@NonNull PersonDTO2 person){
        log.info("Creating person : " + person.toString());
        personRepository.save(personMapper.parseDT0V2Person(person));
        PersonDTO2 returnPersonDto = findAllV2().getLast();
        log.info("Showing person : " + returnPersonDto.toString());
        return returnPersonDto;
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
        return personUpdate;
    }

    public void deleteById(String id){
        log.info("Deleting person : " + id);
        personRepository.deleteById(Long.parseLong(id));
    }
}
