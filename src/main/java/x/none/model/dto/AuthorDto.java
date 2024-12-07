package x.none.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import x.none.model.Author;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorDto {
    private Long id;
    private String name;
    private String lastName;
    private LocalDate dayOfBirth;
    private String country;

    public AuthorDto(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.lastName = author.getLastName();
        this.dayOfBirth = author.getDayOfBirth();
        this.country = author.getCountry();
    }
}
