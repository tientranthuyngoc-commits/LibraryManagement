package com.example.library.controller.librarian;

import com.example.library.domain.Book;
import com.example.library.repository.CategoryRepository;
import com.example.library.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookController {
    private final BookService bookService;
    private final CategoryRepository categoryRepository;

    public BookController(BookService bookService, CategoryRepository categoryRepository) {
        this.bookService = bookService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/books")
    public String list(@RequestParam(value = "q", required = false) String q, Model model) {
        model.addAttribute("books", bookService.findAll(q));
        model.addAttribute("query", q == null ? "" : q);
        return "librarian/books";
    }

    @GetMapping("/books/create")
    public String createForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryRepository.findAll());
        return "librarian/book-form";
    }

    @PostMapping("/books/create")
    public String create(Book book) {
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            book.setCategory(categoryRepository.findById(book.getCategory().getId()).orElse(null));
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/books/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        model.addAttribute("categories", categoryRepository.findAll());
        return "librarian/book-form";
    }

    @PostMapping("/books/{id}/edit")
    public String edit(@PathVariable Long id, Book book) {
        book.setId(id);
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            book.setCategory(categoryRepository.findById(book.getCategory().getId()).orElse(null));
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/delete")
    public String delete(@PathVariable Long id) {
        bookService.delete(id);
        return "redirect:/books";
    }
}
