package com.library.library_management.repo;

import com.library.library_management.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepo extends JpaRepository<Loan, Long> {

    List<Loan> findByPatronIdAndReturnDateIsNull(Long patronId);

    Optional<Loan> findByBookIdAndReturnDateIsNull(Long bookId);


}
