package com.davi.library.repository;

import com.davi.library.domain.Book;

import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> findByIsbn(String isbn);
}
