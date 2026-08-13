package com.davi.library.repository;

import com.davi.library.domain.Reader;

import java.util.Optional;

public interface ReaderRepository {

    Reader save(Reader reader);

    Optional<Reader> findById(Long id);
}
