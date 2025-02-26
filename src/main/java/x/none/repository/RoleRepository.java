package x.none.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import x.none.model.ERole;
import x.none.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

}
