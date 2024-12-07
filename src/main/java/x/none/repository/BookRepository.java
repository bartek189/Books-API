package x.none.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import x.none.model.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select b from Book b left join fetch b.author")
    List<Book> findAllWithAuthor();
}
