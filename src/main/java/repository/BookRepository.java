package repository;

import static javax.sound.midi.ShortMessage.START;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Book;

public class BookRepository {
    private final List<String> categories;
    private final Map<String, List<Book>> booksByCategory;

    public BookRepository() {
        categories = Arrays.asList("IT", "FANTASY", "SCIENCE");

        booksByCategory = new HashMap<>();
        booksByCategory.put("IT", Arrays.asList(
            new Book(1L, "Java8 in action"), new Book(2L, "JUnit in action"), new Book(3L, "Kotlin in action")));

        booksByCategory.put("FANTASY", Arrays.asList(
            new Book(4L, "Harry porter"), new Book(5L, "The Lord of rings"), new Book(6L, "Tom's Midnight Garden")));

        booksByCategory.put("SCIENCE", Arrays.asList(
            new Book(7L, "Nuclear"), new Book(8L, "The Lord of rings"), new Book(9L, "The Earth Our Habitat")));
    }

    public List<String> retrieveCategories() {
        sleep(1000L);
        System.out.println(String.format("Retrieve Category (done in %s)", (System.nanoTime() - START) / 1_000_000));
        return categories;
    }

    public List<Book> retrieveBooksByCategory(String category) {
        sleep(1000L);
        System.out.println(String.format("Retrieve Books [%s] (done in %s)", category, (System.nanoTime() - START) / 1_000_000));
        return booksByCategory.getOrDefault(category, Collections.emptyList());
    }

    public void updateAuthor(Book book, String author) {
        sleep(1000L);
        System.out.println(String.format("%s's author was updated (done in %s)", book.getName(), (System.nanoTime() - START) / 1_000_000));
        book.updateAuthor(author);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
