package com.davi.library.service;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import com.davi.library.domain.Loan;
import com.davi.library.domain.Reader;
import com.davi.library.exception.LoanNotFoundException;
import com.davi.library.repository.JdbcBookRepository;
import com.davi.library.repository.JdbcLoanRepository;
import com.davi.library.repository.JdbcReaderRepository;
import org.junit.jupiter.api.Test;

import static com.davi.library.domain.LoanStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;

public class LoanServiceTest {

    @Test
    void shouldCreateLoan() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        JdbcBookRepository bookRepository = new JdbcBookRepository(connectionFactory);
        JdbcReaderRepository readerRepository = new JdbcReaderRepository(connectionFactory);
        JdbcLoanRepository loanRepository = new JdbcLoanRepository(connectionFactory);

        BookService bookService = new BookService(bookRepository);
        ReaderService readerService = new ReaderService(readerRepository);
        LoanService loanService = new LoanService(loanRepository, bookService, readerService);

        Book book = bookService.create("Loan Service Test Book", "Loan Service Test Author", "isbnLoanServiceTest001");
        Reader reader = readerService.create("Loan Service Test Reader", "loanServiceTest001@gmail.com");

        Loan loan = loanService.create(book.getId(), reader.getId());

        assertNotNull(loan);
        assertNotNull(loan.getId());

        assertEquals(book.getId(), loan.getBookId());
        assertEquals(reader.getId(), loan.getReaderId());
        assertEquals(ACTIVE, loan.getStatus());
    }

    @Test
    void shouldFindLoanById() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        JdbcBookRepository bookRepository = new JdbcBookRepository(connectionFactory);
        JdbcReaderRepository readerRepository = new JdbcReaderRepository(connectionFactory);
        JdbcLoanRepository loanRepository = new JdbcLoanRepository(connectionFactory);

        BookService bookService = new BookService(bookRepository);
        ReaderService readerService = new ReaderService(readerRepository);
        LoanService loanService = new LoanService(loanRepository, bookService, readerService);

        Book book = bookService.create("Find Loan By Id Book", "Find Loan By Id Author", "isbnFindLoanByIdTest001");
        Reader reader = readerService.create("Find Loan By Id Reader", "findLoanByIdTest001@gmail.com");

        Loan loan = loanService.create(book.getId(), reader.getId());

        Loan foundLoan = loanService.findById(loan.getId());

        assertNotNull(foundLoan);
        assertEquals(loan.getId(), foundLoan.getId());
        assertEquals(book.getId(), foundLoan.getBookId());
        assertEquals(reader.getId(), foundLoan.getReaderId());
        assertEquals(ACTIVE, foundLoan.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenFindingNonExistentLoan() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        JdbcLoanRepository loanRepository = new JdbcLoanRepository(connectionFactory);
        JdbcBookRepository bookRepository = new JdbcBookRepository(connectionFactory);
        JdbcReaderRepository readerRepository = new JdbcReaderRepository(connectionFactory);

        BookService bookService = new BookService(bookRepository);
        ReaderService readerService = new ReaderService(readerRepository);
        LoanService loanService = new LoanService(loanRepository, bookService, readerService);

        assertThrows(LoanNotFoundException.class,
                () -> loanService.findById(999999L));
    }
}