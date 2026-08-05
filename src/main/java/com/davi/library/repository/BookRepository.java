package com.davi.library.repository;

import com.davi.library.domain.Book;

public interface BookRepository {
    Book save(Book book);
}
