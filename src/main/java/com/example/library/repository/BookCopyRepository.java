package com.example.library.repository;

import com.example.library.domain.BookCopy;
import com.example.library.domain.enumtype.BookCopyStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findByBookId(Long bookId);
    List<BookCopy> findByStatus(BookCopyStatus status);
    List<BookCopy> findByBarcodeContainingIgnoreCase(String barcode);
}