import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LibraryManager {
    private final List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public int getTotalBooks() {
        return books.size();
    }

    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager();
        manager.addBook(new Book("B001", "Clean Code", "Robert C. Martin"));
        manager.addBook(new Book("B002", "Effective Java", "Joshua Bloch"));

        for (Book book : manager.getBooks()) {
            System.out.println(book);
        }

        System.out.println("Total: " + manager.getTotalBooks());
    }
}