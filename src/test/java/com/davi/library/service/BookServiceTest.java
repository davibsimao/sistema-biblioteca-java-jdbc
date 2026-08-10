package com.davi.library.service;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import com.davi.library.exception.DuplicateIsbnException;
import com.davi.library.repository.JdbcBookRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {
    @Test
    void shouldCreateBook() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcBookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);

        String isbn = "ISBNServicetest-" + System.currentTimeMillis();
        Book book = bookService.create("titleServiceTest", "authorServiceTest", isbn);

        assertNotNull(book);
        assertNotNull(book.getId());
        assertEquals("titleServiceTest", book.getTitle());
        assertEquals("authorServiceTest", book.getAuthor());
        assertEquals(isbn, book.getIsbn());
        assertTrue(book.isAvailable());
    }

    @Test
    void shouldThrowExceptionWhenIsbnAlreadyExists() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcBookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);

        String isbn = "ISBNduplicate-" + System.currentTimeMillis();

        bookService.create("aaa", "aaa", isbn);

        assertThrows(DuplicateIsbnException.class, () -> {
            bookService.create("bbb", "bbb", isbn);
        });
    }

    @Test
    void shouldFindBookById() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcBookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);

        Book book = new Book("titleBookIdTest", "authorBookIdTest", "isbnBookIdTest");
        Book savedBook = repository.save(book);
        Optional<Book> result = bookService.findById(savedBook.getId());

        assertTrue(result.isPresent());
        assertEquals(savedBook.getId(), result.get().getId());
        assertEquals(savedBook.getTitle(), result.get().getTitle());
        assertEquals(savedBook.getAuthor(), result.get().getAuthor());
        assertEquals(savedBook.getIsbn(), result.get().getIsbn());
    }
    @Test
    void shouldReturnEmptyWhenBookDoesNotExist() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcBookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);
        Optional<Book> result = bookService.findById(999999L);

        assertTrue(result.isEmpty());
    }
}