package com.example.library.controller.librarian;

import com.example.library.domain.BookCopy;
import com.example.library.service.BookCopyService;
import com.example.library.service.LoanService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.library.repository.UserRepository;

@Controller
public class LoanController {
    private final LoanService loanService;
    private final BookCopyService bookCopyService;
    private final UserRepository userRepository;

    public LoanController(LoanService loanService, BookCopyService bookCopyService, UserRepository userRepository) {
        this.loanService = loanService;
        this.bookCopyService = bookCopyService;
        this.userRepository = userRepository;
    }

    @GetMapping("/loans")
    public String list(Model model) {
        model.addAttribute("loans", loanService.findAll());
        return "librarian/loans";
    }

    @GetMapping("/loans/overdue")
    public String overdue(Model model) {
        model.addAttribute("loans", loanService.findOverdue());
        return "librarian/loans";
    }

    @GetMapping("/loans/create")
    public String createForm(Model model) {
        model.addAttribute("members", loanService.findMembers());
        model.addAttribute("copies", bookCopyService.findAvailable());
        return "librarian/loan-form";
    }

    @PostMapping("/loans/create")
    public String create(@RequestParam Long memberId,
                         @RequestParam List<Long> copyIds,
                         @RequestParam String dueDate,
                         Authentication authentication) {
        Long librarianId = userRepository.findByUsername(authentication.getName()).orElseThrow().getId();
        loanService.createLoan(memberId, librarianId, copyIds, LocalDate.parse(dueDate));
        return "redirect:/loans";
    }

    @PostMapping("/loans/{id}/return")
    public String returnLoan(@PathVariable Long id) {
        loanService.returnLoan(id);
        return "redirect:/loans";
    }

    @PostMapping("/loans/{id}/renew")
    public String renew(@PathVariable Long id,
                        @RequestParam String newDueDate,
                        @RequestParam(required = false) String note) {
        loanService.renewLoan(id, LocalDate.parse(newDueDate), note);
        return "redirect:/loans";
    }
}
