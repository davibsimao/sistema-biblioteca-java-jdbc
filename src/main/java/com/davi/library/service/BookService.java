package com.davi.library.service;

import com.davi.library.domain.Book;
import com.davi.library.exception.BookNotFoundException;
import com.davi.library.exception.DuplicateIsbnException;
import com.davi.library.repository.BookRepository;

import java.util.List;
import java.util.Optional;

public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book create(String title, String author, String isbn) {
        Optional<Book> findIsbn = bookRepository.findByIsbn(isbn);

        if (findIsbn.isPresent()) {
            throw new DuplicateIsbnException("Book with ISBN " + isbn + " already exists");
        }

        Book book = new Book(title, author, isbn);
        return bookRepository.save(book);

    }

    public Book findById(Long id) {
        Optional<Book> idFound = bookRepository.findById(id);

        if (idFound.isEmpty()) {
            throw new BookNotFoundException("Book with ID: " + id + "not found.");
        }

        return idFound.get();
    }

    public Optional<Book> findByIsbn(String isbn) {return bookRepository.findByIsbn(isbn);}

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book update(Long id, String title, String author, String isbn) {
        Optional<Book> idFound = bookRepository.findById(id);

        if (idFound.isEmpty()) {
            throw new BookNotFoundException("Book with ID: " + id + "not found.");
        }

        Optional<Book> isbnFound = bookRepository.findByIsbn(isbn);

        if (isbnFound.isPresent()) {
            Book book = isbnFound.get();

            if (!book.getId().equals(id)) {
                throw new DuplicateIsbnException("Book with ISBN " + isbn + " already exists");
            }
        }

        return bookRepository.update(id , title, author, isbn);
    }

    public Book delete (Long id) {
        Optional<Book> idFound = bookRepository.findById(id);

        if (idFound.isEmpty()) {
            throw new BookNotFoundException("Book with ID: " + id + "not found.");
        }

        Book book = idFound.get();

        bookRepository.delete(id);

        return book;

    }
}

