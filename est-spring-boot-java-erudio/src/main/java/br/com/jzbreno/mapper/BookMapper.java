package br.com.jzbreno.mapper;

import br.com.jzbreno.model.Book;
import br.com.jzbreno.model.DTO.BookDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BookMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public BookDTO parseBookToBookDTO(Book book) {

        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setTitle(book.getTitle());

        if (book.getLaunch_date() != null) {
            bookDTO.setLaunch_date(String.valueOf(book.getLaunch_date()));
        }

        bookDTO.setPrice(String.valueOf(book.getPrice()));
        return bookDTO;
    }

    public Book parseBookDTOToBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setAuthor(bookDTO.getAuthor());
        book.setTitle(bookDTO.getTitle());
        if (bookDTO.getLaunch_date() != null) {
            book.setLaunch_date(LocalDate.parse(bookDTO.getLaunch_date(), FORMATTER));
        }
        book.setPrice(Double.parseDouble(bookDTO.getPrice()));
        return book;
    }

    public List<Book> parseBookDTOListToBookList(List<BookDTO> bookDTOList) {
        List<Book> listBooks = bookDTOList.stream().map( book -> {
            Book newBook = new Book();
            newBook.setId(book.getId());
            newBook.setAuthor(book.getAuthor());
            newBook.setTitle(book.getTitle());
            newBook.setPrice(Double.parseDouble(book.getPrice()));
            if (book.getLaunch_date() != null) {
                newBook.setLaunch_date(LocalDate.parse(book.getLaunch_date(), FORMATTER));
            }
            return newBook;
        }).toList();

        return listBooks;
    }

    public List<BookDTO> parseBookDTOListToBookDTOList(List<Book> listBooks) {
        List<BookDTO> bookDTOoList =  listBooks.stream().map( book -> {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setId(book.getId());
            bookDTO.setAuthor(book.getAuthor());
            bookDTO.setTitle(book.getTitle());
            if (book.getLaunch_date() != null) {
                bookDTO.setLaunch_date(String.valueOf(book.getLaunch_date()));
            }
            bookDTO.setPrice(String.valueOf(book.getPrice()));
            return bookDTO;
        }).toList();
        return bookDTOoList;
    }

}
