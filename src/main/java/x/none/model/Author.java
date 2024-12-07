package x.none.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private LocalDate dayOfBirth;
    private String country;

    @OneToMany(mappedBy = "author")
    private Set<Book> bookList = new HashSet<>();

    public Author(String name, String lastName, LocalDate dayOfBirth, String country) {
        this.name = name;
        this.lastName = lastName;
        this.dayOfBirth = dayOfBirth;
        this.country = country;
    }
}
