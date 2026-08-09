package com.davi.library.repository;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import com.davi.library.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);

            try (ResultSet rs = ps.executeQuery()) {
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

    @Override
    public List<Book> findAll() {
        String sql = "SELECT id, title, author, isbn, available FROM books";
        List<Book> books = new ArrayList<>();

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Book bookRestore = Book.restore(rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getBoolean("available"));

                    books.add(bookRestore);
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find books", e);
        }
        return books;
    }

    @Override
    public Book update(Long id, String title, String author, String isbn) {
        String selectSql = "SELECT available FROM books WHERE id = ?";
        String updateSql = "UPDATE books SET title = ?, author = ?, isbn = ? WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection()) {

            boolean currentAvailable;
            try (PreparedStatement selectPs = conn.prepareStatement(selectSql)) {
                selectPs.setLong(1, id);
                try (ResultSet rs = selectPs.executeQuery()) {
                    if (!rs.next()) {
                        throw new DataAccessException("Book not found with id: " + id);
                    }
                    currentAvailable = rs.getBoolean("available");
                }
            }

            try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                updatePs.setString(1, title);
                updatePs.setString(2, author);
                updatePs.setString(3, isbn);
                updatePs.setLong(4, id);

                int rowsAffected = updatePs.executeUpdate();

                if (rowsAffected != 1) {
                    throw new DataAccessException("Failed to update book: no rows affected");
                }
            }

            return Book.restore(id, title, author, isbn, currentAvailable);

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update book", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Book not found with id: " + id);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete book", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT id, title, author, isbn, available FROM books WHERE id = ?";

        try (Connection conn = connectionFactory.getConnection();
        PreparedStatement ps  = conn.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
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
        }catch (SQLException e) {
            throw new DataAccessException("Failed to find book by id", e);        }


        return Optional.empty();
    }
}
