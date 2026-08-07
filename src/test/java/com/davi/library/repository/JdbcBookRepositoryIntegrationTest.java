package com.davi.library.repository;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import org.junit.jupiter.api.Test;

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
}