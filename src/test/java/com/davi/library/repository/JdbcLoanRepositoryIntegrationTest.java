package com.davi.library.repository;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import com.davi.library.domain.Loan;
import com.davi.library.domain.Reader;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.davi.library.domain.LoanStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void shouldFindLoanById() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        JdbcBookRepository bookRepository = new JdbcBookRepository(connectionFactory);
        JdbcReaderRepository readerRepository = new JdbcReaderRepository(connectionFactory);
        JdbcLoanRepository loanRepository = new JdbcLoanRepository(connectionFactory);

        Book book = new Book("Find Loan Book", "Find Loan Author", "9780000000002");
        Reader reader = new Reader("Find Loan Reader", "findLoanReader001@gmail.com");

        Book savedBook = bookRepository.save(book);
        Reader savedReader = readerRepository.save(reader);

        Loan loan = new Loan(savedBook.getId(), savedReader.getId());
        Loan savedLoan = loanRepository.save(loan);

        Optional<Loan> foundLoan = loanRepository.findById(savedLoan.getId());

        assertTrue(foundLoan.isPresent());
        assertEquals(savedLoan.getId(), foundLoan.get().getId());
        assertEquals(savedBook.getId(), foundLoan.get().getBookId());
        assertEquals(savedReader.getId(), foundLoan.get().getReaderId());
        assertEquals(ACTIVE, foundLoan.get().getStatus());
    }

    @Test
    void shouldReturnEmptyWhenLoanNotFound() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        JdbcLoanRepository loanRepository = new JdbcLoanRepository(connectionFactory);

        Optional<Loan> foundLoan = loanRepository.findById(999999L);

        assertTrue(foundLoan.isEmpty());
    }
}