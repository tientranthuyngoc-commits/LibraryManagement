package com.example.library.service;

import com.example.library.domain.Book;
import com.example.library.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(String q) {
        if (q == null || q.isBlank()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContainingIgnoreCase(q, q, q);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow();
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}