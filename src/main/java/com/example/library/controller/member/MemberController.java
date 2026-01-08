package com.example.library.controller.member;

import com.example.library.repository.LoanRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.BookService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final BookService bookService;

    public MemberController(UserRepository userRepository, LoanRepository loanRepository, BookService bookService) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.bookService = bookService;
    }

    @GetMapping("/member/profile")
    public String profile(Authentication authentication, Model model) {
        model.addAttribute("user", userRepository.findByUsername(authentication.getName()).orElseThrow());
        return "member/profile";
    }

    @GetMapping("/member/books")
    public String books(@RequestParam(value = "q", required = false) String q, Model model) {
        model.addAttribute("books", bookService.findAll(q));
        model.addAttribute("query", q == null ? "" : q);
        return "member/books";
    }

    @GetMapping("/member/history")
    public String history(Authentication authentication, Model model) {
        Long memberId = userRepository.findByUsername(authentication.getName()).orElseThrow().getId();
        model.addAttribute("loans", loanRepository.findByMemberId(memberId));
        return "member/history";
    }
}