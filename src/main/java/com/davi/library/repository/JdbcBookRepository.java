package com.davi.library.repository;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import com.davi.library.exception.DataAccessException;

import java.sql.*;

public class JdbcBookRepository implements BookRepository {
    private final ConnectionFactory connectionFactory;

    public JdbcBookRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, available) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setBoolean(4, book.isAvailable());


            int rowsAffected = ps.executeUpdate();

            if (rowsAffected != 1) {
                throw new DataAccessException("Failed to save book: no rows affected");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long generatedId = generatedKeys.getLong(1);

                    return Book.restore(generatedId, book.getTitle(), book.getAuthor(), book.getIsbn(), book.isAvailable());
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to save book", e);
        }

        throw new DataAccessException("Failed to save book: generated id not returned");
    }
}
