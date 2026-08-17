package com.davi.library.repository;

import com.davi.library.domain.Loan;

import java.util.Optional;

public interface LoanRepository {
    Loan save (Loan loan);

    Optional<Loan> findById(Long id);


}
