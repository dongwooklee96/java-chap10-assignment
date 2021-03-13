import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import domain.Book;
import repository.BookRepository;

public class HomeWork {
    private final BookRepository repository = new BookRepository();
    private static long START;

    public static void main(String[] args) {
        START = System.nanoTime();
        HomeWork homeWork = new HomeWork();
        homeWork.run("Lee");
    }

    public void run(String author) {
        final ExecutorService executors = Executors.newFixedThreadPool(9);
        updateAllBook(author, executors);
        System.out.println(String.format("Done in %sms", (System.nanoTime() - START) / 1_000_000)); // 완료 로그
    }

    public void updateAllBook(String author, ExecutorService executors) {
        final List<CompletableFuture<List<Book>>> futures
            = repository.retrieveCategories().stream()
            .map(category -> supplyAsync(() -> repository.retrieveBooksByCategory(category), executors))
            .collect(toList());

        final List<CompletableFuture<Void>> updateFutures
            = futures.stream()
            .map(future -> future.thenCompose(books -> allOf(
                books.stream()
                    .map(book -> runAsync(() -> repository.updateAuthor(book, author), executors))
                    .toArray(CompletableFuture[]::new)
            )))
            .collect(toList());
        updateFutures.forEach(CompletableFuture::join);
    }
}
