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
import static com.davi.library.domain.LoanStatus.RETURNED;
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

    @Test
    void shouldFindLoansByStatus() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        JdbcBookRepository bookRepository = new JdbcBookRepository(connectionFactory);
        JdbcReaderRepository readerRepository = new JdbcReaderRepository(connectionFactory);
        JdbcLoanRepository loanRepository = new JdbcLoanRepository(connectionFactory);

        BookService bookService = new BookService(bookRepository);
        ReaderService readerService = new ReaderService(readerRepository);
        LoanService loanService = new LoanService(loanRepository, bookService, readerService);

        Book book1 = bookService.create("FindByStatus Service Book 01", "FindByStatus Service Author 01", "isbnFindByStatusServiceTest001");
        Book book2 = bookService.create("FindByStatus Service Book 02", "FindByStatus Service Author 02", "isbnFindByStatusServiceTest002");

        Reader reader1 = readerService.create("FindByStatus Service Reader 01", "findByStatusServiceTest001@gmail.com");
        Reader reader2 = readerService.create("FindByStatus Service Reader 02", "findByStatusServiceTest002@gmail.com");

        Loan activeLoan = loanService.create(book1.getId(), reader1.getId());
        Loan loanToReturn = loanService.create(book2.getId(), reader2.getId());

        Loan returnedLoan = loanRepository.updateStatus(loanToReturn.getId(), RETURNED);

        List<Loan> activeLoans = loanService.findByStatus(ACTIVE);
        List<Loan> returnedLoans = loanService.findByStatus(RETURNED);

        assertNotNull(activeLoans);
        assertTrue(activeLoans.stream().anyMatch(loan -> loan.getId().equals(activeLoan.getId())));

        assertNotNull(returnedLoans);
        assertTrue(returnedLoans.stream().anyMatch(loan -> loan.getId().equals(returnedLoan.getId())));

        assertTrue(activeLoans.stream().noneMatch(loan -> loan.getId().equals(returnedLoan.getId())));
    }

    @Test
    void shouldUpdateLoanStatus() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        JdbcBookRepository bookRepository = new JdbcBookRepository(connectionFactory);
        JdbcReaderRepository readerRepository = new JdbcReaderRepository(connectionFactory);
        JdbcLoanRepository loanRepository = new JdbcLoanRepository(connectionFactory);

        BookService bookService = new BookService(bookRepository);
        ReaderService readerService = new ReaderService(readerRepository);
        LoanService loanService = new LoanService(loanRepository, bookService, readerService);

        Book book = bookService.create("Update Status Service Book", "Update Status Service Author", "isbnUpdateStatusServiceTest001");
        Reader reader = readerService.create("Update Status Service Reader", "updateStatusServiceTest001@gmail.com");

        Loan loan = loanService.create(book.getId(), reader.getId());

        Loan updatedLoan = loanService.updateStatus(loan.getId(), RETURNED);

        assertNotNull(updatedLoan);
        assertEquals(loan.getId(), updatedLoan.getId());
        assertEquals(book.getId(), updatedLoan.getBookId());
        assertEquals(reader.getId(), updatedLoan.getReaderId());
        assertEquals(RETURNED, updatedLoan.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingStatusOfNonExistentLoan() {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        JdbcBookRepository bookRepository = new JdbcBookRepository(connectionFactory);
        JdbcReaderRepository readerRepository = new JdbcReaderRepository(connectionFactory);
        JdbcLoanRepository loanRepository = new JdbcLoanRepository(connectionFactory);

        BookService bookService = new BookService(bookRepository);
        ReaderService readerService = new ReaderService(readerRepository);
        LoanService loanService = new LoanService(loanRepository, bookService, readerService);

        assertThrows(LoanNotFoundException.class,
                () -> loanService.updateStatus(999999L, RETURNED));
    }
}