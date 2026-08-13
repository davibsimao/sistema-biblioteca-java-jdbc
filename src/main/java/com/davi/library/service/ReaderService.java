package com.davi.library.service;

import com.davi.library.domain.Reader;
import com.davi.library.exception.DuplicateEmailException;
import com.davi.library.repository.ReaderRepository;

import java.util.Optional;

public class ReaderService {
    private final ReaderRepository readerRepository;

    public ReaderService(ReaderRepository readerRepository) {this.readerRepository = readerRepository;}

    public Reader create(String name, String email) {
        Optional<Reader> emailFound = readerRepository.findByEmail(email);

        if (emailFound.isPresent()) {
            throw new DuplicateEmailException("Reader with EMAIL " + email + " already exists");
        }

        Reader reader = new Reader(name, email);

        return readerRepository.save(reader);
    }
}
