## Chapter 10 (Future, CompletableFuture)

비동기 처리와 순서성에 대해서 공부하게 되었으며, `Future`, `Executors`, `ComputableFuture`에 대해서 학습하였습니다.

- 과제는 `JDK15`로 작성되었습니다.
- 과제 진행사항은 블로그에 정리되어있습니다.

## 과제

아래와 같이 각 카테고리 (IT, FANTASY, SCIENCE) 별 세개의 책을 가지고 있는 저장소 BookRepository가 있습니다.

BookRepository는 아래의 API를 제공합니다.

- 카테고리 목록을 조회하는 API retrieveCategories (1000ms 지연)
- 카테고리 별 책 목록을 조회하는 API retrieveBooksByCategory (1000ms 지연)
- 특정 책의 저자를 업데이트 하는 API updateAuthor (1000ms 지연)

**BookRepository를 통해서 모든 책의 저자를 비동기적으로 업데이트 하는 코드를 작성 해 주세요**

- 단, 전체 작업이 완료 되는 시점에 "Done in 3032ms" 와 같이 소요시간을 체크하는 완료 로그를 찍을 수 있어야 합니다. 
- 각 단계별 수행시간을 찍을 수 있게 제가 미리 코드를 작성 해뒀습니다. updateAllBook 내의 소스코드만 채워주시면 됩니다.
- 이 문제를 해결하는 정답은 많습니다.
- 다만 final ExecutorService executors = Executors.newFixedThreadPool(9);와 같이 9개의 스레드로 초기화 한 Thread pool의 활용을 극대화 하여 수행시간을 최대한 단축 할 수 있게 소스코드를 작성 해 주세요.

**BookRepository.class**
```
class BookRepository {
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
}

```

**HomeWork.class**

```
public class Homework {
    private final BookRepository repository = new BookRepository();
    private static long START;
 
    public static void main(String[] args) {
        START = System.nanoTime();
        new Homework().run("Lee");
    }
 
    public void run(String author) {
        final ExecutorService executors = Executors.newFixedThreadPool(9);
        updateAllBook(author, executors);
        System.out.println(String.format("Done in %sms", (System.nanoTime() - START) / 1_000_000)); // 완료 로그
    }
 
    public void updateAllBook(String author, ExecutorService executors) {
        // 이곳에 소스코드를 작성 해 주세요
    }
 
    @Data @Getter @Setter @AllArgsConstructor
    private static class Book {
        private Long key;
        private String name;
        private String author;
 
        public Book(Long key, String name) {
            this.key = key;
            this.name = name;
        }
 
        public void updateAuthor(String author) {
            this.author = author;
        }
    }
 
    private static class BookRepository {
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
    }
 
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
```
## 정답 화면
![image2021-2-14_14-27-44](https://user-images.githubusercontent.com/14002238/110233188-add87280-7f65-11eb-86da-aacef35f993d.png)


