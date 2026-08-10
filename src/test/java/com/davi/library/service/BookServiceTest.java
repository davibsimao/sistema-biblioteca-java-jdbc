package com.davi.library.service;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import com.davi.library.exception.BookNotFoundException;
import com.davi.library.exception.DuplicateIsbnException;
import com.davi.library.repository.BookRepository;
import com.davi.library.repository.JdbcBookRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
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

    @Test
    void shouldFindBookByIsbn() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcBookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);

        Book book = new Book("titleBookIsbnTest", "authorBookIsbnTest", "isbnBookIsbnTest");

        Book savedBook = repository.save(book);

        Optional<Book> result = bookService.findByIsbn(savedBook.getIsbn());

        assertTrue(result.isPresent());
        assertEquals(savedBook.getIsbn(), result.get().getIsbn());
        assertEquals(savedBook.getId(), result.get().getId());
        assertEquals(savedBook.getTitle(), result.get().getTitle());
        assertEquals(savedBook.getAuthor(), result.get().getAuthor());
    }

    @Test
    void shouldReturnEmptyWhenBookIsbnDoesNotExist() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcBookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);
        Optional<Book> result = bookService.findByIsbn("1111111doesNotExist");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindAllBooks() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcBookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);

        Book book1 = new Book("titleBookTest1", "authorBookTest1", "isbnBookTest1");
        Book book2 = new Book("titleBookTest2", "authorBookTest2", "isbnBookTest2");

        Book bookSaved1 = repository.save(book1);
        Book bookSaved2 = repository.save(book2);

        List<Book> findAll = bookService.findAll();

        assertNotNull(findAll);
        assertTrue(findAll.size() >= 2);

        assertTrue(findAll.stream()
                .anyMatch(b -> b.getIsbn().equals(bookSaved1.getIsbn())));

        assertTrue(findAll.stream()
                .anyMatch(b -> b.getIsbn().equals(bookSaved2.getIsbn())));

        assertTrue(findAll.stream()
                .anyMatch(b -> b.getId().equals(bookSaved1.getId())));

        assertTrue(findAll.stream()
                .anyMatch(b -> b.getId().equals(bookSaved2.getId())));
    }

    @Test
    void shouldUpdateBook() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        BookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);

        Book book = new Book("Old book", "Old author", "1000000001");

        Book savedBook = repository.save(book);

        Book updatedBook = bookService.update
                (savedBook.getId(), "New title", "New author", "1000000002");

        assertEquals(savedBook.getId(), updatedBook.getId());
        assertEquals("New title", updatedBook.getTitle());
        assertEquals("New author", updatedBook.getAuthor());
        assertEquals("1000000002", updatedBook.getIsbn());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingBook() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        BookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);

        assertThrows(BookNotFoundException.class,
                () -> bookService.update(999999L, "title", "author", "1000000003"));

    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithDuplicateIsbn() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcBookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);

        Book book1 = new Book("Book 1", "Author 1", "1000000004");
        Book book2 = new Book("Book 2", "Author 2", "1000000005");

        Book savedBook1 = repository.save(book1);
        Book savedBook2 = repository.save(book2);

        assertThrows(DuplicateIsbnException.class,
                () -> bookService.update(savedBook1.getId(), "Updated title", "Updated author", savedBook2.getIsbn()));


    }

    @Test
    void shouldUpdateBookKeepingItsOwnIsbn() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcBookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);

        Book book = new Book(
                "Old Title",
                "Old Author",
                "1000000006"
        );

        Book savedBook = repository.save(book);

        Book updatedBook = bookService.update(
                savedBook.getId(),
                "New Title",
                "New Author",
                savedBook.getIsbn()
        );

        assertEquals(savedBook.getId(), updatedBook.getId());
        assertEquals("New Title", updatedBook.getTitle());
        assertEquals("New Author", updatedBook.getAuthor());
        assertEquals(savedBook.getIsbn(), updatedBook.getIsbn());
    }

    @Test
    void shouldDeleteBook() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        BookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);

        Book book = new Book("Book to delete", "Author", "2000000001");

        Book savedBook = repository.save(book);

        Book deletedBook = bookService.delete(savedBook.getId());

        assertNotNull(deletedBook);

        assertEquals(savedBook.getId(), deletedBook.getId());
        assertEquals(savedBook.getTitle(), deletedBook.getTitle());
        assertEquals(savedBook.getAuthor(), deletedBook.getAuthor());
        assertEquals(savedBook.getIsbn(), deletedBook.getIsbn());

        Optional<Book> result = repository.findById(savedBook.getId());

        assertTrue(result.isEmpty());

    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingBook() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        BookRepository repository = new JdbcBookRepository(connectionFactory);
        BookService bookService = new BookService(repository);

        assertThrows(BookNotFoundException.class,
                () -> bookService.delete(1999999L));
    }
}