package br.com.jzbreno.services;

import br.com.jzbreno.controllers.BookController;
import br.com.jzbreno.mapper.BookMapper;
import br.com.jzbreno.model.DTO.BookDTO;
import br.com.jzbreno.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Service
@Slf4j
public class BookServices {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServices(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = new BookMapper();
    }

    public BookDTO findById(Long id) {
        BookDTO bookDTO = bookMapper.parseBookToBookDTO(bookRepository.findById(id).orElse(null));
        implementsHateoasBook(bookDTO);
        return bookDTO;
    }

    public List<BookDTO> findAllBooks() {
        List<BookDTO> bookList = bookMapper.parseBookDTOListToBookDTOList(bookRepository.findAll());
        implementsHateoasBook(bookList);
        return bookList;
    }

    public BookDTO createBook(BookDTO bookDTO) {
        implementsHateoasBook(bookDTO);
        bookRepository.save(bookMapper.parseBookDTOToBook(bookDTO));

        return bookDTO;
    }

    public BookDTO updating(BookDTO bookDTO) {
        BookDTO bookUpdate = findById(bookDTO.getId());
        bookUpdate.setAuthor(bookDTO.getAuthor());
        bookUpdate.setTitle(bookDTO.getTitle());
        bookUpdate.setPrice(bookDTO.getPrice());
        bookUpdate.setLaunch_date(bookDTO.getLaunch_date());
        implementsHateoasBook(bookUpdate);
        bookRepository.save(bookMapper.parseBookDTOToBook(bookUpdate));
        return bookUpdate;
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
//    public class PcDTO extends RepresentationModel<PcDTO> implements Serializable
//    se deve implementar o HATEOAS fazendo o extends de RepresentationModel
    private static void implementsHateoasBook(BookDTO bookDTO) {
        bookDTO.add(linkTo(methodOn(BookController.class).getBookByID(String.valueOf(bookDTO.getId()))).withSelfRel().withType("GET"));
        bookDTO.add(linkTo(methodOn(BookController.class).findAllBooks()).withRel("findAllBooks").withType("GET"));
        bookDTO.add(linkTo(methodOn(BookController.class).deleteBook(String.valueOf(bookDTO.getId()))).withRel("deleteBook").withType("DELETE"));
        bookDTO.add(linkTo(methodOn(BookController.class).createBook(bookDTO)).withRel("createBook").withType("POST"));
        bookDTO.add(linkTo(methodOn(BookController.class).updateBook(bookDTO)).withRel("update").withType("PUT"));
    }

    private static void implementsHateoasBook(List<BookDTO> bookDTOList) {
        for (BookDTO bookDTO : bookDTOList) {
            implementsHateoasBook(bookDTO);
        }
    }


}
