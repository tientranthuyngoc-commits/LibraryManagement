package com.example.library.config;

import com.example.library.domain.Book;
import com.example.library.domain.BookCopy;
import com.example.library.domain.Category;
import com.example.library.domain.Loan;
import com.example.library.domain.LoanItem;
import com.example.library.domain.Role;
import com.example.library.domain.User;
import com.example.library.domain.enumtype.BookCopyStatus;
import com.example.library.domain.enumtype.LoanStatus;
import com.example.library.domain.enumtype.UserStatus;
import com.example.library.repository.BookCopyRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CategoryRepository;
import com.example.library.repository.LoanRepository;
import com.example.library.repository.RoleRepository;
import com.example.library.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {
    @Bean
    public CommandLineRunner seedData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      CategoryRepository categoryRepository,
                                      BookRepository bookRepository,
                                      BookCopyRepository bookCopyRepository,
                                      LoanRepository loanRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            Role librarianRole = new Role();
            librarianRole.setName("LIBRARIAN");
            Role memberRole = new Role();
            memberRole.setName("MEMBER");
            roleRepository.saveAll(List.of(adminRole, librarianRole, memberRole));

            User admin = new User();
            admin.setUsername("admin");
            admin.setPasswordHash(passwordEncoder.encode("Admin@123"));
            admin.setFullName("Admin User");
            admin.setEmail("admin@example.com");
            admin.setStatus(UserStatus.ACTIVE);
            admin.getRoles().add(adminRole);

            User librarian = new User();
            librarian.setUsername("librarian");
            librarian.setPasswordHash(passwordEncoder.encode("Librarian@123"));
            librarian.setFullName("Librarian User");
            librarian.setEmail("librarian@example.com");
            librarian.setStatus(UserStatus.ACTIVE);
            librarian.getRoles().add(librarianRole);

            User member = new User();
            member.setUsername("member");
            member.setPasswordHash(passwordEncoder.encode("Member@123"));
            member.setFullName("Member User");
            member.setEmail("member@example.com");
            member.setStatus(UserStatus.ACTIVE);
            member.getRoles().add(memberRole);

            userRepository.saveAll(List.of(admin, librarian, member));

            Category cat1 = new Category();
            cat1.setName("Programming");
            Category cat2 = new Category();
            cat2.setName("Business");
            categoryRepository.saveAll(List.of(cat1, cat2));

            Book b1 = createBook("Clean Code", "Robert C. Martin", "9780132350884", "Prentice Hall", 2008, cat1);
            Book b2 = createBook("Effective Java", "Joshua Bloch", "9780134685991", "Addison-Wesley", 2018, cat1);
            Book b3 = createBook("Refactoring", "Martin Fowler", "9780134757599", "Addison-Wesley", 2018, cat1);
            Book b4 = createBook("The Lean Startup", "Eric Ries", "9780307887894", "Crown Business", 2011, cat2);
            Book b5 = createBook("Deep Work", "Cal Newport", "9781455586691", "Grand Central", 2016, cat2);
            bookRepository.saveAll(List.of(b1, b2, b3, b4, b5));

            List<BookCopy> copies = List.of(
                buildCopy(b1, "BC001"), buildCopy(b1, "BC002"),
                buildCopy(b2, "BC003"), buildCopy(b2, "BC004"),
                buildCopy(b3, "BC005"), buildCopy(b3, "BC006"),
                buildCopy(b4, "BC007"), buildCopy(b4, "BC008"),
                buildCopy(b5, "BC009"), buildCopy(b5, "BC010")
            );
            bookCopyRepository.saveAll(copies);

            Loan activeLoan = new Loan();
            activeLoan.setMember(member);
            activeLoan.setLibrarian(librarian);
            activeLoan.setLoanDate(LocalDate.now().minusDays(2));
            activeLoan.setDueDate(LocalDate.now().plusDays(7));
            activeLoan.setStatus(LoanStatus.ACTIVE);

            LoanItem activeItem = new LoanItem();
            activeItem.setLoan(activeLoan);
            BookCopy activeCopy = copies.get(0);
            activeCopy.setStatus(BookCopyStatus.BORROWED);
            activeItem.setBookCopy(activeCopy);
            activeLoan.getItems().add(activeItem);

            Loan returnedLoan = new Loan();
            returnedLoan.setMember(member);
            returnedLoan.setLibrarian(librarian);
            returnedLoan.setLoanDate(LocalDate.now().minusDays(10));
            returnedLoan.setDueDate(LocalDate.now().minusDays(2));
            returnedLoan.setStatus(LoanStatus.RETURNED);

            LoanItem returnedItem = new LoanItem();
            returnedItem.setLoan(returnedLoan);
            BookCopy returnedCopy = copies.get(1);
            returnedCopy.setStatus(BookCopyStatus.AVAILABLE);
            returnedItem.setBookCopy(returnedCopy);
            returnedLoan.getItems().add(returnedItem);

            loanRepository.saveAll(List.of(activeLoan, returnedLoan));
            bookCopyRepository.saveAll(List.of(activeCopy, returnedCopy));
        };
    }

    private Book createBook(String title, String author, String isbn, String publisher, int year, Category category) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPublisher(publisher);
        book.setPublishYear(year);
        book.setDescription("Sample description");
        book.setCategory(category);
        return book;
    }

    private BookCopy buildCopy(Book book, String barcode) {
        BookCopy copy = new BookCopy();
        copy.setBook(book);
        copy.setBarcode(barcode);
        copy.setStatus(BookCopyStatus.AVAILABLE);
        return copy;
    }
}