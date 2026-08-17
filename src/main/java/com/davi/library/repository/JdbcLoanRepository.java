package com.davi.library.repository;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Loan;
import com.davi.library.exception.DataAccessException;

import java.sql.*;

public class JdbcLoanRepository implements LoanRepository{
    private final ConnectionFactory connectionFactory;

    public JdbcLoanRepository(ConnectionFactory connectionFactory) {this.connectionFactory = connectionFactory;}

    @Override
    public Loan save(Loan loan) {
        String sql = "INSERT INTO loans (book_id, reader_id, status) VALUES (?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, loan.getBookId());
            ps.setLong(2, loan.getReaderId());
            ps.setString(3, loan.getStatus().name());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected != 1) {
                throw new DataAccessException("Failed to save loan: no rows affected");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);

                    return Loan.restore(id, loan.getBookId(), loan.getReaderId(), loan.getStatus());
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to save loan", e);
        }

        throw new DataAccessException("Failed to save loan: generated id not returned");
    }
}
