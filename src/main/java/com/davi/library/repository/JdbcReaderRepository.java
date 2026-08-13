package com.davi.library.repository;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Reader;
import com.davi.library.exception.DataAccessException;

import java.sql.*;
import java.util.Optional;

public class JdbcReaderRepository implements ReaderRepository{
    private final ConnectionFactory connectionFactory;

    public JdbcReaderRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Reader save(Reader reader) {
        String sql = "INSERT INTO readers (name, email) VALUES (?, ?)";
        try (Connection conn = connectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, reader.getName());
            ps.setString(2, reader.getEmail());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected != 1) {
                throw new DataAccessException("Failed to save reader: no rows affected");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);

                    return Reader.restore(id, reader.getName(), reader.getEmail());
                }
            }

        }catch (SQLException e) {
            throw new DataAccessException("Failed to save reader", e);
        }

        throw new DataAccessException("Failed to save reader: generated id not returned");
    }

    @Override
    public Optional<Reader> findById(Long id) {
        String sql = "SELECT id, name, email FROM readers WHERE id = ? ";
        try (Connection conn = connectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reader readerRestore = Reader.restore(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"));

                    return Optional.of(readerRestore);
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find reader by id", e);
        }

        return Optional.empty();
    }
}
