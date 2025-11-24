package br.com.jzbreno.controllers;

import br.com.jzbreno.model.DTO.BookDTO;
import br.com.jzbreno.services.BookServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
//@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/book")
@Tag(name = "Book", description = "Enpoint for managing Book testes, version 2 with Content Negotiation and Others tecnologies")
public class BookController implements BookControllerDoc {

    private BookServices bookServices;

    public BookController(BookServices bookServices) {
        this.bookServices = bookServices;
    }

//    @CrossOrigin(origins = {"http://localhost:8081"}) assim adiciona para cada endoint
    @GetMapping(value = "/{id}",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<BookDTO> getBookByID(@PathVariable(name = "id") String id ) {
        BookDTO bookDTO = bookServices.findById(Long.valueOf(id));
        return bookDTO != null ? ResponseEntity.ok(bookDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/findAll"
            , produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<List<BookDTO>> findAllBooks() {
        List<BookDTO> bookList = bookServices.findAllBooks();
        return bookList != null ? ResponseEntity.ok(bookList) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_YAML_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<BookDTO> saveBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok().body(bookServices.createBook(bookDTO));
    }

    @PostMapping(value = "/create",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_YAML_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookServices.createBook(bookDTO));
    }

    @PutMapping(value = "/update",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_YAML_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<BookDTO> updateBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok().body(bookServices.updating(bookDTO));
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> deleteBook(@PathVariable(name = "id") String id) {
        bookServices.deleteById(Long.valueOf(id));
        return ResponseEntity.noContent().build();
    }

}
