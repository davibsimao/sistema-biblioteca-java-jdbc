package com.davi.library.repository;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import com.davi.library.domain.Reader;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class
JdbcReaderRepositoryIntegrationTest {

    @Test
    void shouldSaveReader() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository jdbcReaderRepository = new JdbcReaderRepository(connectionFactory);

        Reader reader = new Reader("davi", "chatgpt@gmail.com");

        Reader savedReader = jdbcReaderRepository.save(reader);

        assertNotNull(savedReader);
        assertNotNull(savedReader.getId());

        assertEquals("davi", savedReader.getName());
        assertEquals("chatgpt@gmail.com", savedReader.getEmail());
    }

    @Test
    void shouldFindReaderById() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository repository = new JdbcReaderRepository(connectionFactory);

        Reader reader = new Reader("nametest", "nametest00@gmail.com");

        Reader savedReader = repository.save(reader);

        Optional<Reader> result = repository.findById(savedReader.getId());

        assertTrue(result.isPresent());

        Reader found = result.get();

        assertEquals(savedReader.getId(), found.getId());
        assertEquals("nametest", found.getName());
        assertEquals("nametest00@gmail.com", found.getEmail());
    }

    @Test
    void shouldReturnEmptyWhenReaderIdDoesNotExist() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository repository = new JdbcReaderRepository(connectionFactory);

        Optional<Reader> result = repository.findById(999999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindReaderByEmail() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository repository = new JdbcReaderRepository(connectionFactory);

        Reader reader = new Reader("nametest", "findReaderByEmailTest@gmail.com");

        Reader savedReader = repository.save(reader);

        Optional<Reader> result = repository.findByEmail(savedReader.getEmail());

        assertTrue(result.isPresent());

        Reader found = result.get();

        assertEquals("nametest", found.getName());
        assertEquals("findReaderByEmailTest@gmail.com", found.getEmail());
    }

    @Test
    void shouldReturnEmptyWhenReaderEmailDoesNotExist() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcReaderRepository repository = new JdbcReaderRepository(connectionFactory);

        Optional<Reader> result = repository.findByEmail("ajofoaaofkam@gmail.com");

        assertTrue(result.isEmpty());

    }

}
