package com.example.library.repository;

import com.example.library.domain.Renewal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RenewalRepository extends JpaRepository<Renewal, Long> {
}