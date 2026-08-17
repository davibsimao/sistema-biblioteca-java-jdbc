package com.davi.library.service;

import com.davi.library.domain.Reader;
import com.davi.library.exception.DuplicateEmailException;
import com.davi.library.exception.ReaderNotFoundException;
import com.davi.library.repository.ReaderRepository;

import java.util.List;
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

    public Reader findById(Long id) {
        Optional<Reader> idFound = readerRepository.findById(id);

        if (idFound.isEmpty()) {
            throw new ReaderNotFoundException("Reader with ID: " + id + "not found.");
        }

        return idFound.get();
    }

    public Optional<Reader> findByEmail(String email) {
        return readerRepository.findByEmail(email);
    }

    public List<Reader> findAll() {
        return readerRepository.findAll();

    }

    public Reader update(Long id, String name, String email) {
        Optional<Reader> idFound = readerRepository.findById(id);

        if (idFound.isEmpty()) {
            throw new ReaderNotFoundException("Reader with ID: " + id + " not found.");
        }

        Optional<Reader> emailFound = readerRepository.findByEmail(email);

        if (emailFound.isPresent()) {
            Reader reader = emailFound.get();

            if (!reader.getId().equals(id)) {
                throw new DuplicateEmailException("Reader with EMAIL " + email + " already exists");
            }
        }
        return readerRepository.update(id, name, email);
    }

    public Reader delete (Long id) {
        Optional<Reader> idFound = readerRepository.findById(id);

        if (idFound.isEmpty()) {
            throw new ReaderNotFoundException("Reader not found with id: " + id);
        }
        Reader reader = idFound.get();

        readerRepository.delete(id);

        return reader;
    }

}
