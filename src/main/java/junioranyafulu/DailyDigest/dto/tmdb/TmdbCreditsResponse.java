package junioranyafulu.DailyDigest.dto.tmdb;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TmdbCreditsResponse {
    private List<TmdbCast> cast;

    @Data
    @NoArgsConstructor
    public static class TmdbCast {
        private String name;
        private String character;
        private String profile_path;
        private Integer order;
    }
}
