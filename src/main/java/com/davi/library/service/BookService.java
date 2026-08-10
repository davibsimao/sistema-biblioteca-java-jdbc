package com.davi.library.service;

import com.davi.library.domain.Book;
import com.davi.library.exception.DuplicateIsbnException;
import com.davi.library.exception.InvalidIsbnFormatException;
import com.davi.library.repository.BookRepository;

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

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
}

