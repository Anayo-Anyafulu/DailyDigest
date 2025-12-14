package junioranyafulu.DailyDigest.dto.rawg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawgGamesResponse {

    private Integer count;

    private String next;

    private String previous;

    private List<RawgGame> results;
}
