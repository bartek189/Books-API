package x.none.launcher;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import x.none.model.Author;
import x.none.repository.AuthorRepository;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class StartUpDataLoader {
    private final AuthorRepository authorRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void Save() {
        Author author = authorRepository.save(new Author("Lilith", "Astra", LocalDate.of(2000, 11, 1), "Poland"
        ));
        Author author2 = authorRepository.save(new Author("Kate", "Ries", LocalDate.of(2000, 11, 1), "Madagascar"
        ));
        Author author3 = authorRepository.save(new Author("Penelopa", "Stephie", LocalDate.of(2000, 11, 1), "Guadeloupe"
        ));
        Author author4 = authorRepository.save(new Author("Michella", "Giustina", LocalDate.of(2000, 11, 1), "Salvador"
        ));
        Author author5 = authorRepository.save(new Author("Felice", "Margarette", LocalDate.of(2000, 11, 1), "Salvador"
        ));
        Author author6 = authorRepository.save(new Author("Ulrike", "Tamar", LocalDate.of(2000, 11, 1), "Salvador"
        ));
        Author author7 = authorRepository.save(new Author("Giustina", "Margarette", LocalDate.of(2000, 11, 1), "Salvador"
        ));
        Author author8 = authorRepository.save(new Author("Michella", "Ulrike", LocalDate.of(2000, 11, 1), "San Marino"
        ));
        Author author9 = authorRepository.save(new Author("Felizio", "Ronna", LocalDate.of(2000, 11, 1), "Macedonia, The Former Yugoslav Republic of"
        ));
        Author author10 = authorRepository.save(new Author("Shaine", "Jorry", LocalDate.of(2000, 11, 1), "Island"
        ));
    }
}
