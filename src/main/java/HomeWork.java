import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
        repository.retrieveCategories().parallelStream()
                .map(category -> CompletableFuture.supplyAsync(() -> repository.retrieveBooksByCategory(category)))
                .collect(Collectors.toList())
        .parallelStream()
        .map(CompletableFuture::join)
        .flatMap(Collection::parallelStream)
        .collect(Collectors.toList())
        .parallelStream()
        .map(book -> CompletableFuture.runAsync(() -> repository.updateAuthor(book, author), executors))
        .collect(Collectors.toList())
        .forEach(CompletableFuture::join);
        executors.shutdown();
    }
}
