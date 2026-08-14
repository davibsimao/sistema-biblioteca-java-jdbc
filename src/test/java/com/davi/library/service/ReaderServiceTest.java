package com.davi.library.service;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Reader;
import com.davi.library.exception.DuplicateEmailException;
import com.davi.library.exception.ReaderNotFoundException;
import com.davi.library.repository.JdbcReaderRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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

    @Test
    void shouldFindReaderByEmail() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository repository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(repository);

        Reader reader = readerService.create("ReaderFindByEmailServiceTest", "readerFindByEmailServiceTest@gmail.com");

        Optional<Reader> result = readerService.findByEmail(reader.getEmail());

        assertTrue(result.isPresent());

        Reader foundReader = result.get();

        assertEquals(reader.getId(), foundReader.getId());
        assertEquals(reader.getName(), foundReader.getName());
        assertEquals(reader.getEmail(), foundReader.getEmail());
    }

    @Test
    void shouldReturnEmptyWhenReaderEmailDoesNotExist() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository repository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(repository);

        Optional<Reader> result = readerService.findByEmail("readerEmailDoesNotExistServiceTest@gmail.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindAllReaders() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository repository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(repository);

        Reader reader1 = readerService.create("ReaderFindAllServiceTest01", "readerFindAllServiceTest01@gmail.com");

        Reader reader2 = readerService.create("ReaderFindAllServiceTest02", "readerFindAllServiceTest02@gmail.com");

        List<Reader> readers = readerService.findAll();

        assertNotNull(readers);

        boolean containsReader1 = readers.stream()
                .anyMatch(reader -> reader.getId().equals(reader1.getId()));

        boolean containsReader2 = readers.stream()
                .anyMatch(reader -> reader.getId().equals(reader2.getId()));

        assertTrue(containsReader1);
        assertTrue(containsReader2);
    }

    @Test
    void shouldUpdateReader() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository jdbcReaderRepository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(jdbcReaderRepository);

        Reader reader = readerService.create("ReaderServiceUpdateName01", "readerServiceUpdateOld01@gmail.com");

        Reader updatedReader = readerService.update(reader.getId(), "ReaderServiceUpdatedName01", "readerServiceUpdateNew01@gmail.com");

        assertEquals("ReaderServiceUpdatedName01", updatedReader.getName());

        assertEquals("readerServiceUpdateNew01@gmail.com", updatedReader.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentReader() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository jdbcReaderRepository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(jdbcReaderRepository);

        assertThrows(ReaderNotFoundException.class,
                () -> readerService.update(9999999L, "ReaderNonExistentServiceTest", "readerNonExistentServiceTest@gmail.com"));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithDuplicateEmail() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository jdbcReaderRepository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(jdbcReaderRepository);

        String email = "readerDuplicateEmailServiceTest@gmail.com";

        Reader reader1 = readerService.create("ReaderDuplicateEmail01", "readerDuplicateEmail01@gmail.com");

        readerService.create("ReaderDuplicateEmail02", email);

        assertThrows(DuplicateEmailException.class,
                () -> readerService.update(reader1.getId(), "ReaderDuplicateEmail", email));

    }

    @Test
    void shouldUpdateReaderKeepingItsOwnEmail() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository repository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(repository);

        Reader reader = readerService.create("ReaderKeepEmailServiceTest", "readerKeepEmailServiceTest@gmail.com");

        Reader updatedReader = readerService.update(reader.getId(), "ReaderKeepEmailUpdated", reader.getEmail());

        assertEquals(reader.getId(), updatedReader.getId());
        assertEquals("ReaderKeepEmailUpdated", updatedReader.getName());
        assertEquals(reader.getEmail(), updatedReader.getEmail());
    }

    @Test
    void shouldDeleteReader() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository repository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(repository);

        Reader reader = readerService.create("ReaderDeleteTest01", "ReaderDeleteTest01@gmail.com");

        Reader deletedReader = readerService.delete(reader.getId());

        assertNotNull(deletedReader);

        assertEquals(reader.getId(), deletedReader.getId());
        assertEquals(reader.getName(), deletedReader.getName());
        assertEquals(reader.getEmail(), deletedReader.getEmail());

        assertThrows(ReaderNotFoundException.class,
                () -> readerService.findById(reader.getId()));
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentReader() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository repository = new JdbcReaderRepository(connectionFactory);
        ReaderService readerService = new ReaderService(repository);

        assertThrows(ReaderNotFoundException.class,
                () -> readerService.delete(999999999L));
    }
}