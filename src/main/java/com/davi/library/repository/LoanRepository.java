package com.davi.library.repository;

import com.davi.library.domain.Loan;
import com.davi.library.domain.LoanStatus;

import java.util.List;
import java.util.Optional;

public interface LoanRepository {
    Loan save (Loan loan);

    Optional<Loan> findById(Long id);

    List<Loan> findAll();

    List<Loan> findByStatus(LoanStatus status);

    Loan updateStatus(Long id, LoanStatus newStatus);
}
