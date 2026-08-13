package com.davi.library.service;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Reader;
import com.davi.library.exception.DuplicateEmailException;
import com.davi.library.exception.ReaderNotFoundException;
import com.davi.library.repository.JdbcReaderRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReaderServiceTest {

    @Test
    void shouldCreateReader() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository jdbcReaderRepository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(jdbcReaderRepository);

        Reader reader = readerService.create("nameReaderServiceTest", "emailReaderServiceTestt@gmail.com");

        assertNotNull(reader);
        assertNotNull(reader.getId());

        assertEquals("nameReaderServiceTest", reader.getName());
        assertEquals("emailReaderServiceTestt@gmail.com", reader.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository jdbcReaderRepository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(jdbcReaderRepository);

        String email = "emailduplicado2@gmail.com";

        readerService.create("NameServiceTest01", email);

        assertThrows(DuplicateEmailException.class,
                () -> readerService.create("NameServiceTest02", email));

    }

    @Test
    void shouldFindReaderById() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository repository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(repository);

        Reader reader = readerService.create("ReaderFindByIdServiceTest", "readerFindByIdServiceTest@gmail.com");

        Reader foundReader = readerService.findById(reader.getId());

        assertEquals(reader.getId(), foundReader.getId());
        assertEquals(reader.getName(), foundReader.getName());
        assertEquals(reader.getEmail(), foundReader.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistentReader() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository repository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(repository);

        assertThrows(ReaderNotFoundException.class,
                () -> readerService.findById(999999L));
    }
}
