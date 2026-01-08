package com.example.library.controller.librarian;

import com.example.library.domain.BookCopy;
import com.example.library.domain.enumtype.BookCopyStatus;
import com.example.library.repository.BookRepository;
import com.example.library.service.BookCopyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CopyController {
    private final BookCopyService bookCopyService;
    private final BookRepository bookRepository;

    public CopyController(BookCopyService bookCopyService, BookRepository bookRepository) {
        this.bookCopyService = bookCopyService;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/copies")
    public String list(@RequestParam(value = "bookId", required = false) Long bookId,
                       @RequestParam(value = "q", required = false) String q,
                       Model model) {
        if (bookId != null) {
            model.addAttribute("copies", bookCopyService.findByBook(bookId));
            model.addAttribute("book", bookRepository.findById(bookId).orElseThrow());
        } else {
            model.addAttribute("copies", bookCopyService.searchByBarcode(q));
        }
        model.addAttribute("query", q == null ? "" : q);
        return "librarian/copies";
    }

    @GetMapping("/copies/create")
    public String createForm(@RequestParam("bookId") Long bookId, Model model) {
        BookCopy copy = new BookCopy();
        copy.setBook(bookRepository.findById(bookId).orElseThrow());
        model.addAttribute("copy", copy);
        model.addAttribute("statuses", BookCopyStatus.values());
        return "librarian/copy-form";
    }

    @PostMapping("/copies/create")
    public String create(BookCopy copy, @RequestParam("bookId") Long bookId) {
        copy.setBook(bookRepository.findById(bookId).orElseThrow());
        bookCopyService.save(copy);
        return "redirect:/copies?bookId=" + bookId;
    }

    @GetMapping("/copies/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        BookCopy copy = bookCopyService.findById(id);
        model.addAttribute("copy", copy);
        model.addAttribute("statuses", BookCopyStatus.values());
        return "librarian/copy-form";
    }

    @PostMapping("/copies/{id}/edit")
    public String edit(@PathVariable Long id, BookCopy copy) {
        copy.setId(id);
        bookCopyService.save(copy);
        return "redirect:/copies?bookId=" + copy.getBook().getId();
    }

    @PostMapping("/copies/{id}/delete")
    public String delete(@PathVariable Long id) {
        Long bookId = bookCopyService.findById(id).getBook().getId();
        bookCopyService.delete(id);
        return "redirect:/copies?bookId=" + bookId;
    }
}
