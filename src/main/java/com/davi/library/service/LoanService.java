package com.davi.library.service;

import com.davi.library.domain.Book;
import com.davi.library.domain.Loan;
import com.davi.library.domain.Reader;
import com.davi.library.exception.BookNotFoundException;
import com.davi.library.exception.ReaderNotFoundException;
import com.davi.library.repository.LoanRepository;

import java.util.Optional;

public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final ReaderService readerService;

    public LoanService(LoanRepository loanRepository, BookService bookService, ReaderService readerService) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.readerService = readerService;
    }

    public Loan create(Long bookId, Long readerId) {
        Book book = bookService.findById(bookId);
        Reader reader = readerService.findById(readerId);

        Loan loan = new Loan(book.getId(), reader.getId());

        return loanRepository.save(loan);
    }
}
