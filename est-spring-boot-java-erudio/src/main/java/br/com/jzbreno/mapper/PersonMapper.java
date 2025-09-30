package br.com.jzbreno.mapper;

import br.com.jzbreno.model.DTO.PersonDTO2;
import br.com.jzbreno.model.Person;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Service
public class PersonMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PersonDTO2 parsePersonDTOV2(Person person) {

        PersonDTO2 personDtoV2 = new PersonDTO2();
        personDtoV2.setFirstName(person.getFirstName());
        personDtoV2.setLastName(person.getLastName());
        personDtoV2.setGender(person.getGender());
        personDtoV2.setId(person.getId());
        personDtoV2.setAddress(person.getAddress());

        // CORREÇÃO: Converte a String para LocalDate e verifica por null
        if (person.getBirthday() != null) {
            personDtoV2.setBirthday(person.getBirthday());
        }

        return personDtoV2;
    }

    public Person parseDT0V2Person(PersonDTO2 personDTO2) {
        Person person = new Person();
        person.setFirstName(personDTO2.getFirstName());
        person.setLastName(personDTO2.getLastName());
        person.setGender(personDTO2.getGender());
        person.setId(personDTO2.getId());
        person.setAddress(personDTO2.getAddress());
        person.setBirthday(LocalDate.parse(personDTO2.getBirthday().format(FORMATTER), FORMATTER.withZone(ZoneId.systemDefault())));

        // CORREÇÃO: Converte o LocalDate para String e verifica por null
        if (personDTO2.getBirthday() != null) {
            person.setBirthday(person.getBirthday());
        }

        return person;
    }

    public List<PersonDTO2> parseListPersonDTOV2(List<Person> persons) {

        List<PersonDTO2> personsDtoV2 = persons.stream().map( person -> {
            PersonDTO2 personDtoV2 = new PersonDTO2();
            personDtoV2.setFirstName(person.getFirstName());
            personDtoV2.setLastName(person.getLastName());
            personDtoV2.setGender(person.getGender());
            personDtoV2.setId(person.getId());
            personDtoV2.setAddress(person.getAddress());

            // CORREÇÃO: Converte a String para LocalDate e verifica por null
            if (person.getBirthday() != null) {
                personDtoV2.setBirthday(person.getBirthday());
            }
            return personDtoV2;
        }).toList();

        return personsDtoV2;

    }

    public List<Person> parseListPersonV2(List<PersonDTO2> personsDtoV2) {

        List<Person> persons = new ArrayList<>();

        persons = personsDtoV2.stream().map(this::parseDT0V2Person).toList();

        return persons;

    }

}
