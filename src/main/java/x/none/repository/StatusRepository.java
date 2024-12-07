package x.none.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import x.none.model.StatusInfo;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<StatusInfo, Long> {

    Optional<StatusInfo> findFirstByJobId(Long id);
}
