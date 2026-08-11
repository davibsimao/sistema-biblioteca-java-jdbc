package com.davi.library.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReaderTest {
    @Test
    void shouldCreateReaderWithValidData() {
        Reader reader = new Reader("davi", "davi@gmail.com");

        assertEquals("davi", reader.getName());
        assertEquals("davi@gmail.com", reader.getEmail());
    }

    @Test
    void shouldRejectNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Reader(null, "readerEmailTest01@gmail.com"));
    }

    @Test
    void shouldRejectBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Reader(" ","readerEmailTest02@gmail.com"));
    }

    @Test
    void shouldRejectNullEmail() {
        assertThrows(IllegalArgumentException.class,
                () -> new Reader("readerNameTest01", null));
    }

    @Test
    void shouldRejectBlankEmail () {
        assertThrows(IllegalArgumentException.class,
                () -> new Reader("readerNameTest02", " "));

    }

    @Test
    void shouldRejectInvalidEmail () {
        assertThrows(IllegalArgumentException.class,
                () -> new Reader("readerNameTest03", "10ei--1>!dm91ja"));
    }

    @Test
    void shouldRejectNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> Reader.restore(null, "name", "email"));
    }

    @Test
    void shouldRejectNegativeIdOrRejectZeroId() {
        assertThrows(IllegalArgumentException.class,
                () -> Reader.restore(-1L, "name", "email"));

        assertThrows(IllegalArgumentException.class,
                () -> Reader.restore(0L, "name", "email"));
    }

    @Test
    void shouldRestoreReaderWithDatabaseId() {
        Reader readerRestore = Reader.restore(1L, "davi", "davi@gmail.com");

        assertEquals(1L, readerRestore.getId());
        assertEquals("davi", readerRestore.getName());
        assertEquals("davi@gmail.com", readerRestore.getEmail());
    }




}
