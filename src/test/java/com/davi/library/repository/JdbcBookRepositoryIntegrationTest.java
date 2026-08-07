package com.davi.library.repository;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JdbcBookRepositoryIntegrationTest {

    @Test
    void shouldSaveBook() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcBookRepository repository = new JdbcBookRepository(connectionFactory);
        Book book = new Book("Clean Code", "Robert Martin", "ISBN-" + System.currentTimeMillis());

        Book savedBook = repository.save(book);

        assertNotNull(savedBook);
        assertNotNull(savedBook.getId());

        assertEquals(book.getTitle(), savedBook.getTitle());
        assertEquals(book.getAuthor(), savedBook.getAuthor());
        assertEquals(book.getIsbn(), savedBook.getIsbn());
        assertEquals(book.isAvailable(), savedBook.isAvailable());
    }

    @Test
    void shouldFindBook() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcBookRepository repository = new JdbcBookRepository(connectionFactory);

        Book book = new Book("titleTest", "authorTest", "ISBNtest-" + System.currentTimeMillis());
        Book saved = repository.save(book);

        Optional<Book> result = repository.findByIsbn(book.getIsbn());

        assertTrue(result.isPresent());

        Book found = result.get();
        assertEquals(saved.getId(), found.getId());
        assertEquals(book.getTitle(), found.getTitle());
        assertEquals(book.getAuthor(), found.getAuthor());
        assertEquals(book.getIsbn(), found.getIsbn());
        assertEquals(book.isAvailable(), found.isAvailable());
    }
}