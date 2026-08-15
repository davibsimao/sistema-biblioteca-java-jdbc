package com.davi.library.domain;

import org.junit.jupiter.api.Test;

import static com.davi.library.domain.LoanStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoanTest {
    @Test
    void shouldCreateLoanWithValidData() {
        Long bookId = 1L;
        Long readerId = 1L;
        Loan loan = new Loan(bookId, readerId);

        assertEquals(bookId, loan.getBookId());
        assertEquals(readerId, loan.getReaderId());
        assertEquals(ACTIVE,loan.getStatus());
    }

    @Test
    void shouldRejectNullBookId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Loan(null, 1L));
    }

    @Test
    void shouldRejectInvalidBookId () {
        assertThrows(IllegalArgumentException.class,
                () -> new Loan(0L, 1L));

        assertThrows(IllegalArgumentException.class,
                () -> new Loan(-1L, 1L));
    }

    @Test
    void shouldRejectNullReaderId() {
        assertThrows(IllegalArgumentException.class,
                () -> new Loan(1L, null));
    }

    @Test
    void shouldRejectInvalidReaderId () {
        assertThrows(IllegalArgumentException.class,
                () -> new Loan(1L, 0L));

        assertThrows(IllegalArgumentException.class,
                () -> new Loan(1L, -1L));
    }

    @Test
    void shouldRestoreLoanWithDatabaseData() {
        Loan loanRestore = Loan.restore(1L, 1L, 1L, ACTIVE);

        assertEquals(1L, loanRestore.getId());
        assertEquals(1L, loanRestore.getBookId());
        assertEquals(1L, loanRestore.getReaderId());
        assertEquals(ACTIVE, loanRestore.getStatus());
    }

    @Test
    void shouldRejectNullStatus() {
        assertThrows(IllegalArgumentException.class,
                () -> Loan.restore(1L, 1L, 1L, null));
    }
}
