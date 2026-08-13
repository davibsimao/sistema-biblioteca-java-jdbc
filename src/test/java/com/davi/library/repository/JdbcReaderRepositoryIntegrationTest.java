package com.davi.library.repository;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Reader;
import org.junit.jupiter.api.Test;

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

}
