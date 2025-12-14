package junioranyafulu.DailyDigest.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  
@AllArgsConstructor
@Builder
public class NewsApiResponse {

    private String status;
    private int totalResults;
    private List<Article> articles;

}
