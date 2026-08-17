package com.davi.library.repository;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import com.davi.library.domain.Loan;
import com.davi.library.domain.Reader;
import org.junit.jupiter.api.Test;

import static com.davi.library.domain.LoanStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JdbcLoanRepositoryIntegrationTest {

    @Test
    void shouldSaveLoan() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        JdbcBookRepository bookRepository = new JdbcBookRepository(connectionFactory);
        JdbcReaderRepository readerRepository = new JdbcReaderRepository(connectionFactory);
        JdbcLoanRepository loanRepository = new JdbcLoanRepository(connectionFactory);

        Book book = new Book("Loan Test Book", "Loan Test Author", "9780000000001");

        Reader reader = new Reader("Loan Test Reader", "loanTestReader001@gmail.com");

        Book savedBook = bookRepository.save(book);
        Reader savedReader = readerRepository.save(reader);

        Loan loan = new Loan(savedBook.getId(), savedReader.getId());

        Loan savedLoan = loanRepository.save(loan);

        assertNotNull(savedLoan);
        assertNotNull(savedLoan.getId());
        assertNotNull(savedLoan.getStatus());

        assertEquals(savedBook.getId(), savedLoan.getBookId());
        assertEquals(savedReader.getId(), savedLoan.getReaderId());
        assertEquals(ACTIVE, savedLoan.getStatus());
    }
}