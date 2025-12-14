package junioranyafulu.DailyDigest.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "digest_sections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigestSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "digest_id", nullable = false)
    private Digest digest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SectionType sectionType;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(length = 20000)
    private String content;

    @Lob
    @Column(length = 50000)
    private String rawData;

    private Integer displayOrder;

    private Integer itemCount;
}
