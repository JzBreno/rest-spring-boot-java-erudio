package br.com.jzbreno.repository;

import br.com.jzbreno.model.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)//essa anotacao integra o springframework com junit 5
@DataJpaTest//cong de teste para trabalhar com jpa carrega tudo necessario para testar o banco persistencia(beans, entidade e etc) e usa um banco proprio
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)//desativa o banco aux para usar o banco atual postgress, mysql ect
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)//define a ordem de execucao dos testes
class PersonRepositoryTest {

    @Autowired
    PersonRepository personRepository;
    private static Person person;

    @BeforeAll
    static void setUp() {
        person = new Person();
    }

    @Test
    @Order(2)//definindo ordem de cada um dos testes
    void disablePerson() {
    }

    @Test
    @Order(1)//definindo ordem de cada um dos testes
    void findPersonByFirstName() {
        Pageable pageableValue = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "firstName"));
        person = personRepository.findPersonByFirstName("iko", pageableValue).getContent().get(0);
        Assertions.assertNotNull(person);
        Assertions.assertEquals(person.getFirstName(), "iko");
    }
}