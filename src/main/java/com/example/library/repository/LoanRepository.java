package com.example.library.repository;

import com.example.library.domain.Loan;
import com.example.library.domain.enumtype.LoanStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByStatus(LoanStatus status);
    List<Loan> findByMemberId(Long memberId);
    List<Loan> findByStatusAndDueDateBefore(LoanStatus status, LocalDate dueDate);
}