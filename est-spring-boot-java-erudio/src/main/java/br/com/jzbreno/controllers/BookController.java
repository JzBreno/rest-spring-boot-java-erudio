package br.com.jzbreno.controllers;

import br.com.jzbreno.model.DTO.BookDTO;
import br.com.jzbreno.repository.BookRepository;
import br.com.jzbreno.services.BookServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/book")
@Slf4j
public class BookController {

    private BookServices bookServices;

    public BookController(BookServices bookServices) {
        this.bookServices = bookServices;
    }

    @GetMapping(value = "/{id}",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<BookDTO> getBookByID(@PathVariable(name = "id") String id ) {
        BookDTO bookDTO = bookServices.findById(Long.valueOf(id));
        return bookDTO != null ? ResponseEntity.ok(bookDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/findAll"
                , produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<List<BookDTO>> findAllBooks() {
        List<BookDTO> bookList = bookServices.findAllBooks();
        return bookList != null ? ResponseEntity.ok(bookList) : ResponseEntity.notFound().build();
    }

}
