package junioranyafulu.DailyDigest.dto.tmdb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TmdbMovieResponse {

    private Integer page;

    private List<TmdbMovie> results;

    private Integer totalPages;

    private Integer totalResults;
}
