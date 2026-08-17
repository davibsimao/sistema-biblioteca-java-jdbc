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

import java.util.List;

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

    @Test
    void shouldFindAllLoans() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        JdbcBookRepository bookRepository = new JdbcBookRepository(connectionFactory);
        JdbcReaderRepository readerRepository = new JdbcReaderRepository(connectionFactory);
        JdbcLoanRepository loanRepository = new JdbcLoanRepository(connectionFactory);

        BookService bookService = new BookService(bookRepository);
        ReaderService readerService = new ReaderService(readerRepository);
        LoanService loanService = new LoanService(loanRepository, bookService, readerService);

        Book book1 = bookService.create("FindAll Loan Book 01", "FindAll Loan Author 01", "isbnFindAllLoanTest001");
        Book book2 = bookService.create("FindAll Loan Book 02", "FindAll Loan Author 02", "isbnFindAllLoanTest002");

        Reader reader1 = readerService.create("FindAll Loan Reader 01", "findAllLoanTest001@gmail.com");
        Reader reader2 = readerService.create("FindAll Loan Reader 02", "findAllLoanTest002@gmail.com");

        Loan loan1 = loanService.create(book1.getId(), reader1.getId());
        Loan loan2 = loanService.create(book2.getId(), reader2.getId());

        List<Loan> loans = loanService.findAll();

        assertNotNull(loans);

        boolean containsLoan1 = loans.stream()
                .anyMatch(loan -> loan.getId().equals(loan1.getId()));

        boolean containsLoan2 = loans.stream()
                .anyMatch(loan -> loan.getId().equals(loan2.getId()));

        assertTrue(containsLoan1);
        assertTrue(containsLoan2);
    }
}