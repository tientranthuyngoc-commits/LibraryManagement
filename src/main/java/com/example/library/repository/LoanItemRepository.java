package com.example.library.repository;

import com.example.library.domain.LoanItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanItemRepository extends JpaRepository<LoanItem, Long> {
}