package com.davi.library.domain;

import java.util.regex.Pattern;

public class Reader {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private Long id;
    private String name;
    private String email;

    public static Reader restore(Long id, String name, String email) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id must be greater than zero");
        }

        Reader reader = new Reader(name, email);
        reader.id = id;

        return reader;
    }

    public Reader(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("The email must be in a valid format.");
        }

        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}