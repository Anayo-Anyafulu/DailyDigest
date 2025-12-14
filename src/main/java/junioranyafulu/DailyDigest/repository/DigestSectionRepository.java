package junioranyafulu.DailyDigest.repository;

import junioranyafulu.DailyDigest.domain.DigestSection;
import junioranyafulu.DailyDigest.domain.SectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DigestSectionRepository extends JpaRepository<DigestSection, Long> {

    List<DigestSection> findByDigestIdOrderByDisplayOrder(Long digestId);

    List<DigestSection> findByDigestIdAndSectionType(Long digestId, SectionType sectionType);
}
