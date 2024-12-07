package x.none.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import x.none.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);
}
