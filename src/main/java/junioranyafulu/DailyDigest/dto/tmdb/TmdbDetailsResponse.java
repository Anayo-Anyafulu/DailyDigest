package junioranyafulu.DailyDigest.dto.tmdb;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TmdbDetailsResponse {
    private TmdbVideosResponse videos;
    private TmdbCreditsResponse credits;
}
