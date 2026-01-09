package com.library.library_management.controller;

import com.library.library_management.Service.LoanService;
import com.library.library_management.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<Loan> checkoutBook(@RequestParam Long bookId, @RequestParam Long patronId) {
        Loan newLoan = loanService.checkoutBook(bookId, patronId);
        return ResponseEntity.status(201).body(newLoan);
    }

    @PutMapping("/return/{loanId}")
    public ResponseEntity<Loan> returnBook(@PathVariable Long loanId) {
        Loan updatedLoan = loanService.returnBook(loanId);
        return ResponseEntity.ok(updatedLoan);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Loan>> getActiveLoans() {
        List<Loan> activeLoans = loanService.getAllActiveLoans();
        return ResponseEntity.ok(activeLoans);
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }
}
