package com.example.library.service;

import com.example.library.domain.enumtype.BookCopyStatus;
import com.example.library.domain.enumtype.LoanStatus;
import com.example.library.repository.BookCopyRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.UserRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public DashboardService(BookRepository bookRepository, BookCopyRepository bookCopyRepository,
                            LoanRepository loanRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
    }

    public long countBooks() {
        return bookRepository.count();
    }

    public long countCopies() {
        return bookCopyRepository.count();
    }

    public long countBorrowed() {
        return bookCopyRepository.findByStatus(BookCopyStatus.BORROWED).size();
    }

    public long countOverdue() {
        return loanRepository.findByStatusAndDueDateBefore(LoanStatus.ACTIVE, LocalDate.now()).size();
    }

    public long countMembers() {
        return userRepository.findAll().stream()
            .filter(user -> user.getRoles().stream().anyMatch(r -> r.getName().equals("MEMBER")))
            .count();
    }
}