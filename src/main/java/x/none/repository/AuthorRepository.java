package x.none.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import x.none.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
