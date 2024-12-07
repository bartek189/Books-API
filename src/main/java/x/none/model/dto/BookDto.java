package x.none.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookDto {
    private Long id;
    private String title;
    private String category;
    private double price;
    private long authorId;

    public BookDto(Long id, String title, String category, double price, long authorId) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
        this.authorId = authorId;
    }
}
