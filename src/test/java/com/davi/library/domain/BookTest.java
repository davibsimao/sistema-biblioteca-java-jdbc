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

    @Test
    void shouldRestoreBookWithDatabaseId() {
        Book book = Book.restore(1L, "Clean Code", "Robert C. Martin", "978-0132350884", false);

        Assertions.assertEquals(1L, book.getId());
        Assertions.assertFalse(book.isAvailable());
    }

    @Test
    void shouldRejectNullId() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Book.restore(null, "title", "author", "isbn", false));
    }

    @Test
    void shouldRejectNegativeId() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Book.restore((long) -1, "title", "author", "isbn", false));
    }

    @Test
    void shouldRejectZeroId() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Book.restore(0L, "title", "author", "isbn", false));

    }




}