package com.davi.library.repository;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Reader;
import com.davi.library.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public Optional<Reader> findByEmail(String email) {
        String sql = "SELECT id, name, email FROM readers WHERE email = ?";

        try (Connection conn = connectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reader readerRestore = Reader.restore(rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"));

                    return Optional.of(readerRestore);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find reader by email", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Reader> findAll() {
        String sql = "SELECT id, name, email FROM readers";

        List<Reader> readers = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reader readerRestore = Reader.restore(rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"));

                    readers.add(readerRestore);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find readers", e);
        }
        return readers;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM readers WHERE id = ?";
        try (Connection conn = connectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Reader not found with id: " + id);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete readers", e);
        }
    }

    @Override
    public Reader update(Long id, String name, String email) {
        String sql = "UPDATE readers SET name = ?, email = ? WHERE id = ? ";

        try (Connection conn = connectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setLong(3, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected != 1) {
                throw new DataAccessException("Failed to update reader: no rows affected");
            }

            return Reader.restore(id, name, email);

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update reader", e);
        }
    }
}
