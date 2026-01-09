package com.library.library_management.Service;

import com.library.library_management.model.Book;
import com.library.library_management.model.Loan;
import com.library.library_management.model.Patron;
import com.library.library_management.repo.BookRepo;
import com.library.library_management.repo.LoanRepo;
import com.library.library_management.repo.PatronRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepo loanRepo;
    private final BookRepo bookRepo;
    private final PatronRepo patronRepo;

    @Autowired
    public LoanService(LoanRepo loanRepo, BookRepo bookRepo, PatronRepo patronRepo) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
        this.patronRepo = patronRepo;
    }

    @Transactional
    public Loan checkoutBook(Long bookId, Long patronId) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        Patron patron = patronRepo.findById(patronId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patron not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No available copies of this book remaining");
        }

        // Reduce available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepo.save(book);

        // Set dates
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(14);

        // Create and save new loan
        Loan newLoan = new Loan();
        newLoan.setBook(book);
        newLoan.setPatron(patron);
        newLoan.setLoanDate(loanDate);
        newLoan.setDueDate(dueDate);
        newLoan.setFinalAmount(0.0);

        return loanRepo.save(newLoan);
    }

    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found"));

        if (loan.getReturnDate() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book has already been returned");
        }

        LocalDate returnDate = LocalDate.now();
        loan.setReturnDate(returnDate);

        LocalDate dueDate = loan.getDueDate();
        if (returnDate.isAfter(dueDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
            double fine = daysOverdue * 0.50; // ₹0.50 per day
            loan.setFinalAmount(fine);
        } else {
            loan.setFinalAmount(0.0);
        }

        // Increase available copies
        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepo.save(book);

        return loanRepo.save(loan);
    }

    public List<Loan> getAllActiveLoans() {
        return loanRepo.findAll().stream()
                .filter(loan -> loan.getReturnDate() == null)
                .toList();
    }

    public List<Loan> getAllLoans() {
        return loanRepo.findAll();
    }
}
