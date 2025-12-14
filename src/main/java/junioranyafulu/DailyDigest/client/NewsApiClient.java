package junioranyafulu.DailyDigest.client;

import junioranyafulu.DailyDigest.dto.NewsApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;

@Service
@Slf4j
public class NewsApiClient {

    private final WebClient webClient;

    @Value("${api.newsapi.key}")
    private String apiKey;

    @Value("${api.newsapi.category}")
    private String category;

    @Value("${api.newsapi.country}")
    private String country;

    @Value("${api.newsapi.page-size}")
    private Integer pageSize;

    public NewsApiClient(@Value("${api.newsapi.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(Objects.requireNonNull(baseUrl))
                .build();
    }

    public NewsApiResponse getTopHeadlines() {
        log.info("Fetching top entertainment headlines from NewsAPI");

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/top-headlines")
                            .queryParam("country", country)
                            .queryParam("category", category)
                            .queryParam("pageSize", pageSize)
                            .queryParam("apiKey", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(NewsApiResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .doOnSuccess(response -> log.info("Successfully fetched {} articles",
                            response != null && response.getArticles() != null ? response.getArticles().size() : 0))
                    .doOnError(error -> log.error("Error fetching top headlines: {}", error.getMessage()))
                    .onErrorResume(e -> {
                        log.warn("Returning empty response due to error");
                        return Mono.just(NewsApiResponse.builder().build());
                    })
                    .block();
        } catch (Exception e) {
            log.error("Exception while fetching top headlines", e);
            return NewsApiResponse.builder().build();
        }
    }

    public NewsApiResponse searchEntertainmentNews(String query) {
        log.info("Searching entertainment news for: {}", query);

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/everything")
                            .queryParam("q", query)
                            .queryParam("language", "en")
                            .queryParam("sortBy", "publishedAt")
                            .queryParam("pageSize", pageSize)
                            .queryParam("apiKey", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(NewsApiResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .doOnSuccess(response -> log.info("Successfully fetched {} search results",
                            response != null && response.getArticles() != null ? response.getArticles().size() : 0))
                    .doOnError(error -> log.error("Error searching news: {}", error.getMessage()))
                    .onErrorResume(e -> {
                        log.warn("Returning empty response due to error");
                        return Mono.just(NewsApiResponse.builder().build());
                    })
                    .block();
        } catch (Exception e) {
            log.error("Exception while searching news", e);
            return NewsApiResponse.builder().build();
        }
    }
}
