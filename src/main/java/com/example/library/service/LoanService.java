package com.example.library.service;

import com.example.library.domain.BookCopy;
import com.example.library.domain.Loan;
import com.example.library.domain.LoanItem;
import com.example.library.domain.Renewal;
import com.example.library.domain.User;
import com.example.library.domain.enumtype.BookCopyStatus;
import com.example.library.domain.enumtype.LoanStatus;
import com.example.library.repository.BookCopyRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.RenewalRepository;
import com.example.library.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;
    private final RenewalRepository renewalRepository;

    public LoanService(LoanRepository loanRepository, BookCopyRepository bookCopyRepository,
                       UserRepository userRepository, RenewalRepository renewalRepository) {
        this.loanRepository = loanRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
        this.renewalRepository = renewalRepository;
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public List<Loan> findOverdue() {
        return loanRepository.findByStatusAndDueDateBefore(LoanStatus.ACTIVE, LocalDate.now());
    }

    public Loan findById(Long id) {
        return loanRepository.findById(id).orElseThrow();
    }

    public List<User> findMembers() {
        return userRepository.findAll().stream()
            .filter(user -> user.getRoles().stream().anyMatch(r -> r.getName().equals("MEMBER")))
            .toList();
    }

    @Transactional
    public Loan createLoan(Long memberId, Long librarianId, List<Long> copyIds, LocalDate dueDate) {
        User member = userRepository.findById(memberId).orElseThrow();
        User librarian = userRepository.findById(librarianId).orElseThrow();

        Loan loan = new Loan();
        loan.setMember(member);
        loan.setLibrarian(librarian);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(dueDate);
        loan.setStatus(LoanStatus.ACTIVE);

        for (Long copyId : copyIds) {
            BookCopy copy = bookCopyRepository.findById(copyId).orElseThrow();
            copy.setStatus(BookCopyStatus.BORROWED);
            bookCopyRepository.save(copy);

            LoanItem item = new LoanItem();
            item.setLoan(loan);
            item.setBookCopy(copy);
            loan.getItems().add(item);
        }

        return loanRepository.save(loan);
    }

    @Transactional
    public void returnLoan(Long loanId) {
        Loan loan = findById(loanId);
        for (LoanItem item : loan.getItems()) {
            item.setReturnedAt(LocalDateTime.now());
            BookCopy copy = item.getBookCopy();
            copy.setStatus(BookCopyStatus.AVAILABLE);
            bookCopyRepository.save(copy);
        }
        loan.setStatus(LoanStatus.RETURNED);
        loanRepository.save(loan);
    }

    @Transactional
    public void renewLoan(Long loanId, LocalDate newDueDate, String note) {
        Loan loan = findById(loanId);
        Renewal renewal = new Renewal();
        renewal.setLoan(loan);
        renewal.setOldDueDate(loan.getDueDate());
        renewal.setNewDueDate(newDueDate);
        renewal.setRenewedAt(LocalDateTime.now());
        renewal.setNote(note);
        renewalRepository.save(renewal);
        loan.setDueDate(newDueDate);
        loanRepository.save(loan);
    }
}