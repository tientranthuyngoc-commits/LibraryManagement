package com.example.library.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "renewals")
public class Renewal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Column(name = "old_due_date", nullable = false)
    private LocalDate oldDueDate;

    @Column(name = "new_due_date", nullable = false)
    private LocalDate newDueDate;

    @Column(name = "renewed_at", nullable = false)
    private LocalDateTime renewedAt;

    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public LocalDate getOldDueDate() {
        return oldDueDate;
    }

    public void setOldDueDate(LocalDate oldDueDate) {
        this.oldDueDate = oldDueDate;
    }

    public LocalDate getNewDueDate() {
        return newDueDate;
    }

    public void setNewDueDate(LocalDate newDueDate) {
        this.newDueDate = newDueDate;
    }

    public LocalDateTime getRenewedAt() {
        return renewedAt;
    }

    public void setRenewedAt(LocalDateTime renewedAt) {
        this.renewedAt = renewedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}