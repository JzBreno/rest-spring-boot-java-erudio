package br.com.jzbreno.services;
import static org.mockito.ArgumentMatchers.anyLong;
import br.com.jzbreno.model.Person;
import br.com.jzbreno.repository.PersonRepository;
import br.com.jzbreno.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//adicionamos que os objetos intanciados aqui so irao ter seu ciclo de vida nessa classe
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//adicionaremos o mockito para testar os metodos da classe
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    //adicionaremos nossos mocks de parametros para nossos teste
    MockPerson input;

    //iremos adicionar a isntancia da classe original que estamos testando, como mock
    @InjectMocks
    PersonServices service;

    //adicionaremos nossos objetos mockados para nossos testes, a anotacao @mock da a liberdade de instanciar objetos
    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        //sem essa abertura de mocks, nao sera possivel instanciar os objetos mockados acima, repository e service SE NAO HOUVER O @ExtendWith(MockitoExtension.class)
//        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {

        //criando um objeto mockado para ser retornado pelo repository simulando o acesso ao banco de dados
        Person person = input.mockEntity(1);
        // garante que o stub será usado
        when(repository.findById(anyLong())).thenReturn(java.util.Optional.of(person));

        var result = service.findById("1");

        //verificando se o objeto retornado pelo metodo findById da classe service foi instanciado corretamente e esta todo preenchido
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
    void createV1() {
    }

    @Test
    void updating() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void findAll() {
    }
}