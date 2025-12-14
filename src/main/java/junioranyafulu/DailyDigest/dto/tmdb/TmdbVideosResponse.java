package junioranyafulu.DailyDigest.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TmdbVideosResponse {
    private List<TmdbVideo> results;

    @Data
    @NoArgsConstructor
    public static class TmdbVideo {
        private String key;
        private String site;
        private String type;
        @JsonProperty("official")
        private boolean official;
    }
}
