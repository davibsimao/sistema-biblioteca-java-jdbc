package com.davi.library.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class BookTest {

    @Test
    void shouldRejectNullTitle() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Book(null, "author", "111"));

    }

    @Test
    void shouldRejectBlankTitle() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Book(" ", "author", "111"));
    }

    @Test
    void shouldRejectNullAuthor() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Book("title", null, "111"));
    }

    @Test
    void shouldRejectBlankAuthor() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Book("title", " ", "111"));
    }

    @Test
    void shouldRejectNullIsbn() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Book("title", "author", null));
    }

    @Test
    void shouldRejectBlankIsbn() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Book("title", "author", " "));
    }

    @Test
    void shouldCreateBookWithValidData() {
        Book book = new Book("Clean Code", "Robert C. Martin", "978-0132350884");
        Assertions.assertEquals("Clean Code", book.getTitle());
        Assertions.assertEquals("Robert C. Martin", book.getAuthor());
        Assertions.assertEquals("978-0132350884", book.getIsbn());

    }

    @Test
    void shouldCreateBookWithoutId() {
        Book book = new Book("title", "author", "isbn");
        Assertions.assertNull(book.getId());

    }


}