package junioranyafulu.DailyDigest.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "digests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Digest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    private String title;

    @Lob
    @Column(length = 100000)
    private String htmlContent;

    @Lob
    @Column(length = 50000)
    private String summary;

    @OneToMany(mappedBy = "digest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DigestSection> sections = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private DigestStatus status = DigestStatus.DRAFT;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addSection(DigestSection section) {
        sections.add(section);
        section.setDigest(this);
    }

    public void removeSection(DigestSection section) {
        sections.remove(section);
        section.setDigest(null);
    }
}
