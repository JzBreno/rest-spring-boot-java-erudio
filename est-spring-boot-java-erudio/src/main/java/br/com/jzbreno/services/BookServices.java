package br.com.jzbreno.services;

import br.com.jzbreno.mapper.BookMapper;
import br.com.jzbreno.model.DTO.BookDTO;
import br.com.jzbreno.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return bookDTO;
    }

    public List<BookDTO> findAllBooks() {
        List<BookDTO> bookList = bookMapper.parseBookDTOListToBookDTOList(bookRepository.findAll());
        return bookList;
    }

    public BookDTO createBook(BookDTO bookDTO) {

        bookRepository.save(bookMapper.parseBookDTOToBook(bookDTO));

        return bookDTO;
    }

    public BookDTO updating(BookDTO bookDTO) {
        BookDTO bookUpdate = findById(bookDTO.getId());
        bookUpdate.setAuthor(bookDTO.getAuthor());
        bookUpdate.setTitle(bookDTO.getTitle());
        bookUpdate.setPrice(bookDTO.getPrice());
        bookUpdate.setLaunch_date(bookDTO.getLaunch_date());
        bookRepository.save(bookMapper.parseBookDTOToBook(bookUpdate));
        return bookUpdate;
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

}
