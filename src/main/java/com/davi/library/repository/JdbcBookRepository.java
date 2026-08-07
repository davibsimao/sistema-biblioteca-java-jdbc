package com.davi.library.repository;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import com.davi.library.exception.DataAccessException;

import java.sql.*;
import java.util.Optional;

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

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        String sql = "SELECT id, title, author, isbn, available FROM books WHERE isbn = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1,isbn);


            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    Book book = Book.restore(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("available"));

                    return Optional.of(book);

                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find book by ISBN", e);
        }

        return Optional.empty();
    }
}
