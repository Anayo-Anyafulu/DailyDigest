package junioranyafulu.DailyDigest.repository;

import junioranyafulu.DailyDigest.domain.Digest;
import junioranyafulu.DailyDigest.domain.DigestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DigestRepository extends JpaRepository<Digest, Long> {

    Optional<Digest> findByDate(LocalDate date);

    List<Digest> findByStatusOrderByDateDesc(DigestStatus status);

    List<Digest> findAllByOrderByDateDesc();

    @Query("SELECT d FROM Digest d WHERE d.status = 'COMPLETED' ORDER BY d.date DESC LIMIT 1")
    Optional<Digest> findLatestCompleted();

    boolean existsByDate(LocalDate date);

    List<Digest> findTop10ByStatusOrderByDateDesc(DigestStatus status);
}
