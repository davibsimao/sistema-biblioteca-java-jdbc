package com.davi.library.service;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import com.davi.library.exception.DuplicateIsbnException;
import com.davi.library.repository.JdbcBookRepository;
import org.junit.jupiter.api.Test;

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
}