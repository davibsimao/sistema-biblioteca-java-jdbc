package com.davi.library.domain;

import static com.davi.library.domain.LoanStatus.ACTIVE;

public class Loan {
    private Long id;
    private Long bookId;
    private Long readerId;
    private LoanStatus status = ACTIVE;

    public static Loan restore(Long id, Long bookId, Long readerId, LoanStatus status) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id must be greater than zero");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status must not be null");
        }

        Loan loan = new Loan(bookId, readerId);
        loan.id = id;
        loan.status = status;

        return loan;
    }

    public Loan(Long bookId, Long readerId) {
        if (bookId == null || bookId <= 0) {
            throw new IllegalArgumentException("Id Book must be greater than zero");
        }

        if (readerId == null || readerId <= 0) {
            throw new IllegalArgumentException("Id reader must be greater than zero");
        }

        this.bookId = bookId;
        this.readerId = readerId;
    }

    public Long getId() {
        return id;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getReaderId() {
        return readerId;
    }

    public LoanStatus getStatus() {
        return status;
    }
}
