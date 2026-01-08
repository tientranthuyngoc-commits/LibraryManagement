package com.example.library.service;

import com.example.library.domain.BookCopy;
import com.example.library.domain.enumtype.BookCopyStatus;
import com.example.library.repository.BookCopyRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookCopyService {
    private final BookCopyRepository bookCopyRepository;

    public BookCopyService(BookCopyRepository bookCopyRepository) {
        this.bookCopyRepository = bookCopyRepository;
    }

    public List<BookCopy> findByBook(Long bookId) {
        if (bookId == null) {
            return bookCopyRepository.findAll();
        }
        return bookCopyRepository.findByBookId(bookId);
    }

    public List<BookCopy> findAvailable() {
        return bookCopyRepository.findByStatus(BookCopyStatus.AVAILABLE);
    }

    public List<BookCopy> searchByBarcode(String q) {
        if (q == null || q.isBlank()) {
            return bookCopyRepository.findAll();
        }
        return bookCopyRepository.findByBarcodeContainingIgnoreCase(q);
    }

    public BookCopy findById(Long id) {
        return bookCopyRepository.findById(id).orElseThrow();
    }

    public BookCopy save(BookCopy copy) {
        return bookCopyRepository.save(copy);
    }

    public void delete(Long id) {
        bookCopyRepository.deleteById(id);
    }
}
