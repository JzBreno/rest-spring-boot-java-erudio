package br.com.jzbreno.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import br.com.jzbreno.Exceptions.RequiredObjectIsNullException;
import br.com.jzbreno.model.DTO.PersonDTO;
import br.com.jzbreno.model.Person;
import br.com.jzbreno.repository.PersonRepository;
import br.com.jzbreno.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Usando ciclo de vida padrão (uma instância por teste) para evitar estado compartilhado
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    PersonServices service;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(repository);
    }

    @Test
    void findById() {
        Person person = input.mockEntity(1);
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));

        var result = service.findById("1");

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("self")
                        && link.getHref().endsWith("/person/v1/1")
                        && link.getType().equals("GET")));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("findAll")
                        && link.getHref().endsWith("/person/v1/findAll")
                        && link.getType().equals("GET")));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("deleteById")
                        && link.getHref().endsWith("/person/v1/1")
                        && link.getType().equals("DELETE")));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("createV1")
                        && link.getHref().endsWith("/person/v1")
                        && link.getType().equals("POST")));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("update")
                        && link.getHref().endsWith("/person")
                        && link.getType().equals("PUT")));
        assertEquals(person.getId(), result.getId());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getGender(), result.getGender());
        assertEquals(person.getAddress(), result.getAddress());
        assertEquals(person.getBirthday(), result.getBirthday());
    }

    @Test
    void createV1WithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> service.createV1(null));
        assertEquals("It is not allowed to persist a null object", exception.getMessage());
    }

    @Test
    void updateV1WithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> service.updating(null));
        assertEquals("It is not allowed to persist a null object", exception.getMessage());
    }

    @Test
    void createV1() {
        Person person = input.mockEntity(1);
        Person persisted = person;
        PersonDTO personDTO = input.mockDTO(1);

        when(repository.save(any(Person.class))).thenReturn(persisted);

        var result = service.createV1(personDTO);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("self")
                        && link.getHref().endsWith("/person/v1/1")
                        && link.getType().equals("GET")));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("findAll")
                        && link.getHref().endsWith("/person/v1/findAll")
                        && link.getType().equals("GET")));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("deleteById")
                        && link.getHref().endsWith("/person/v1/1")
                        && link.getType().equals("DELETE")));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("createV1")
                        && link.getHref().endsWith("/person/v1")
                        && link.getType().equals("POST")));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("update")
                        && link.getHref().endsWith("/person")
                        && link.getType().equals("PUT")));
        assertEquals(person.getId(), result.getId());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getGender(), result.getGender());
        assertEquals(person.getAddress(), result.getAddress());
        assertEquals(person.getBirthday(), result.getBirthday());
    }

    @Test
    void updating() {
        Person person = input.mockEntity(1);
        Person persisted = person;
        PersonDTO personDTO = input.mockDTO(1);

        // O updating costuma fazer findById(...) e depois save(...), então stub ambos
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));
        when(repository.save(any(Person.class))).thenReturn(persisted);

        var result = service.updating(personDTO);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("self")
                        && link.getHref().endsWith("/person/v1/1")
                        && link.getType().equals("GET")));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("findAll")
                        && link.getHref().endsWith("/person/v1/findAll")
                        && link.getType().equals("GET")));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("deleteById")
                        && link.getHref().endsWith("/person/v1/1")
                        && link.getType().equals("DELETE")));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("createV1")
                        && link.getHref().endsWith("/person/v1")
                        && link.getType().equals("POST")));
        assertNotNull(result.getLinks().stream()
                .anyMatch(link -> link.getRel().equals("update")
                        && link.getHref().endsWith("/person")
                        && link.getType().equals("PUT")));
        assertEquals(person.getId(), result.getId());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getGender(), result.getGender());
        assertEquals(person.getAddress(), result.getAddress());
        assertEquals(person.getBirthday(), result.getBirthday());
    }

    @Test
    void deleteById() {
        Person person = input.mockEntity(1);
        when(repository.findById(anyLong())).thenReturn(Optional.of(person));

        service.deleteById("1");

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(repository);
    }

//    @Test
//    @Disabled("reason: still under development")
//    void findAll() {
//        List<Person> list = input.mockEntityList();
//        when(repository.findAll()).thenReturn(list);
//        List<PersonDTO> people = service.findAll();
//
//        assertNotNull(people);
//        assertEquals(14, people.size());
//
//        var personOne = people.get(1);
//
//        assertNotNull(personOne);
//        assertNotNull(personOne.getId());
//        assertNotNull(personOne.getLinks());
//
//        assertNotNull(personOne.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("self")
//                        && link.getHref().endsWith("/api/person/v1/1")
//                        && link.getType().equals("GET")
//                ));
//
//        assertNotNull(personOne.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("findAll")
//                        && link.getHref().endsWith("/api/person/v1")
//                        && link.getType().equals("GET")
//                )
//        );
//
//        assertNotNull(personOne.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("create")
//                        && link.getHref().endsWith("/api/person/v1")
//                        && link.getType().equals("POST")
//                )
//        );
//
//        assertNotNull(personOne.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("update")
//                        && link.getHref().endsWith("/api/person/v1")
//                        && link.getType().equals("PUT")
//                )
//        );
//
//        assertNotNull(personOne.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("delete")
//                        && link.getHref().endsWith("/api/person/v1/1")
//                        && link.getType().equals("DELETE")
//                )
//        );
//
//        assertEquals("Address Test1", personOne.getAddress());
//        assertEquals("First Name Test1", personOne.getFirstName());
//        assertEquals("Last Name Test1", personOne.getLastName());
//        assertEquals("Female", personOne.getGender());
//
//        var personFour = people.get(4);
//
//        assertNotNull(personFour);
//        assertNotNull(personFour.getId());
//        assertNotNull(personFour.getLinks());
//
//        assertNotNull(personFour.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("self")
//                        && link.getHref().endsWith("/api/person/v1/4")
//                        && link.getType().equals("GET")
//                ));
//
//        assertNotNull(personFour.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("findAll")
//                        && link.getHref().endsWith("/api/person/v1")
//                        && link.getType().equals("GET")
//                )
//        );
//
//        assertNotNull(personFour.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("create")
//                        && link.getHref().endsWith("/api/person/v1")
//                        && link.getType().equals("POST")
//                )
//        );
//
//        assertNotNull(personFour.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("update")
//                        && link.getHref().endsWith("/api/person/v1")
//                        && link.getType().equals("PUT")
//                )
//        );
//
//        assertNotNull(personFour.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("delete")
//                        && link.getHref().endsWith("/api/person/v1/4")
//                        && link.getType().equals("DELETE")
//                )
//        );
//
//        assertEquals("Address Test4", personFour.getAddress());
//        assertEquals("First Name Test4", personFour.getFirstName());
//        assertEquals("Last Name Test4", personFour.getLastName());
//        assertEquals("Male", personFour.getGender());
//
//        var personSeven = people.get(7);
//
//        assertNotNull(personSeven);
//        assertNotNull(personSeven.getId());
//        assertNotNull(personSeven.getLinks());
//
//        assertNotNull(personSeven.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("self")
//                        && link.getHref().endsWith("/api/person/v1/7")
//                        && link.getType().equals("GET")
//                ));
//
//        assertNotNull(personSeven.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("findAll")
//                        && link.getHref().endsWith("/api/person/v1")
//                        && link.getType().equals("GET")
//                )
//        );
//
//        assertNotNull(personSeven.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("create")
//                        && link.getHref().endsWith("/api/person/v1")
//                        && link.getType().equals("POST")
//                )
//        );
//
//        assertNotNull(personSeven.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("update")
//                        && link.getHref().endsWith("/api/person/v1")
//                        && link.getType().equals("PUT")
//                )
//        );
//
//        assertNotNull(personSeven.getLinks().stream()
//                .anyMatch(link -> link.getRel().value().equals("delete")
//                        && link.getHref().endsWith("/api/person/v1/7")
//                        && link.getType().equals("DELETE")
//                )
//        );
//
//        assertEquals("Address Test7", personSeven.getAddress());
//        assertEquals("First Name Test7", personSeven.getFirstName());
//        assertEquals("Last Name Test7", personSeven.getLastName());
//        assertEquals("Female", personSeven.getGender());
//    }
}
