package com.davi.library.service;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.domain.Book;
import com.davi.library.domain.Loan;
import com.davi.library.domain.Reader;
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
}