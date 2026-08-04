package com.davi.library.domain;

public class Book {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private boolean available = true;

    public Book(Long id, String title, String author, String isbn) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }

        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author must not be blank");
        }

        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN must not be blank");
        }

        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isAvailable() {
        return available;
    }
}