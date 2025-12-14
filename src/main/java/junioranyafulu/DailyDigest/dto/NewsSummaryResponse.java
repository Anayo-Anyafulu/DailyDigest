package junioranyafulu.DailyDigest.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NewsSummaryResponse {
    private String summary;
    private LocalDateTime createdAt;

}
