package br.com.jzbreno.services;

import br.com.jzbreno.mapper.BookMapper;
import br.com.jzbreno.model.Book;
import br.com.jzbreno.model.DTO.BookDTO;
import br.com.jzbreno.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookServices {
    //findbyid, findall
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    public BookServices(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = new BookMapper();
    }

    public BookDTO findById(Long id) {
        BookDTO bookDTO = bookMapper.parseBookToBookDTO(bookRepository.findById(id).orElse(null));
        return bookDTO;
    }

    public List<BookDTO> findAllBooks() {
        List<BookDTO> bookList = bookMapper.parseBookDTOListToBookDTOList(bookRepository.findAll());
        return bookList;
    }

}
