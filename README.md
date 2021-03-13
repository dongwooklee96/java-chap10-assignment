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


## 피드백


내가 제출한 소스 코드에서는 총 두군데의 `Join`이 존재한다.
세미나에서 join은 현재 Thread를 블록킹 하여, 결과를 기다리는 연산이라고 하였다.
따라서 이를 보완하기 위해서 `CompletableFuture`는 `join` 없이 `CompletionStage` 라는 약속을 기반으로 `Non blocking` 기반의 비동기 처리를 할 수 있게 도와준다.

또한 제출한 소스코드에서는, 스트림의 중간 연산 단계에서 `join`을 호출하고 있는데, 이렇게 하면 `stream`의 게으른 연산을 하는 특징을 인해서 순차적인 `Blocking`이 걸릴 수 있다.
따라서 `join` 연산은 스트림의 수집이 List와 같이 완료된 후에 개별적으로 루프를 돌면서 수행하는 것이 좋다.

그리고 `stream`으로 변환할 때, `parallelStream`은 조심해서 사용해야한다. `parallelStream`을 통해 작업을 멀티 코어에서 병렬 처리를 하는 것은 일반적으로 많은 데이터일 때, 유리하다. 데이터가 적으면 오히려 분할하는데 많은 시간이 걸린다.
이런 것은 여러번 수행해보면서 반드시 측정을 기반으로 선택이 되어야한다. 그러한 경우가 아니라면, `stream`도 충분하다.
결론은 꼭 필요한 곳이 아니라면 `join`연산은 최종적으로 동기가 필요한 곳에서 사용하는 것이 좋다.




