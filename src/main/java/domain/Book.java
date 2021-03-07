package domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
    private Long key;
    private String name;
    private String author;

    public Book(long key, String name) {
        this.key = key;
        this.name = name;
    }

    public void updateAuthor(String author) {
        this.author = author;
    }
}
