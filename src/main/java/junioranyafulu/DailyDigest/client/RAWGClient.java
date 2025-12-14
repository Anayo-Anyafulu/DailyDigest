package junioranyafulu.DailyDigest.client;

import junioranyafulu.DailyDigest.dto.rawg.RawgGamesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class RAWGClient {

    private final WebClient webClient;

    @Value("${api.rawg.key}")
    private String apiKey;

    @Value("${api.rawg.page-size}")
    private Integer pageSize;

    public RAWGClient(@Value("${api.rawg.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public RawgGamesResponse getTrendingGames() {
        log.info("Fetching trending games from RAWG");

        // Get games from the last 30 days, ordered by popularity
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(30);

        String dates = thirtyDaysAgo.format(DateTimeFormatter.ISO_LOCAL_DATE) + "," +
                today.format(DateTimeFormatter.ISO_LOCAL_DATE);

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/games")
                            .queryParam("key", apiKey)
                            .queryParam("dates", dates)
                            .queryParam("ordering", "-added")
                            .queryParam("page_size", pageSize)
                            .build())
                    .retrieve()
                    .bodyToMono(RawgGamesResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .doOnSuccess(response -> log.info("Successfully fetched {} trending games",
                            response != null && response.getResults() != null ? response.getResults().size() : 0))
                    .doOnError(error -> log.error("Error fetching trending games: {}", error.getMessage()))
                    .onErrorResume(e -> {
                        log.warn("Returning empty response due to error");
                        return Mono.just(RawgGamesResponse.builder().build());
                    })
                    .block();
        } catch (Exception e) {
            log.error("Exception while fetching trending games", e);
            return RawgGamesResponse.builder().build();
        }
    }

    public RawgGamesResponse getNewReleases() {
        log.info("Fetching new game releases from RAWG");

        // Get games released in the last 7 days
        LocalDate today = LocalDate.now();
        LocalDate sevenDaysAgo = today.minusDays(7);

        String dates = sevenDaysAgo.format(DateTimeFormatter.ISO_LOCAL_DATE) + "," +
                today.format(DateTimeFormatter.ISO_LOCAL_DATE);

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/games")
                            .queryParam("key", apiKey)
                            .queryParam("dates", dates)
                            .queryParam("ordering", "-released")
                            .queryParam("page_size", pageSize)
                            .build())
                    .retrieve()
                    .bodyToMono(RawgGamesResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .doOnSuccess(response -> log.info("Successfully fetched {} new releases",
                            response != null && response.getResults() != null ? response.getResults().size() : 0))
                    .doOnError(error -> log.error("Error fetching new releases: {}", error.getMessage()))
                    .onErrorResume(e -> {
                        log.warn("Returning empty response due to error");
                        return Mono.just(RawgGamesResponse.builder().build());
                    })
                    .block();
        } catch (Exception e) {
            log.error("Exception while fetching new releases", e);
            return RawgGamesResponse.builder().build();
        }
    }

    public RawgGamesResponse getUpcomingGames() {
        log.info("Fetching upcoming games from RAWG");

        // Get games to be released in the next 60 days
        LocalDate today = LocalDate.now();
        LocalDate sixtyDaysFromNow = today.plusDays(60);

        String dates = today.format(DateTimeFormatter.ISO_LOCAL_DATE) + "," +
                sixtyDaysFromNow.format(DateTimeFormatter.ISO_LOCAL_DATE);

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/games")
                            .queryParam("key", apiKey)
                            .queryParam("dates", dates)
                            .queryParam("ordering", "-rating")
                            .queryParam("page_size", pageSize)
                            .build())
                    .retrieve()
                    .bodyToMono(RawgGamesResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .doOnSuccess(response -> log.info("Successfully fetched {} upcoming games",
                            response != null && response.getResults() != null ? response.getResults().size() : 0))
                    .doOnError(error -> log.error("Error fetching upcoming games: {}", error.getMessage()))
                    .onErrorResume(e -> {
                        log.warn("Returning empty response due to error");
                        return Mono.just(RawgGamesResponse.builder().build());
                    })
                    .block();
        } catch (Exception e) {
            log.error("Exception while fetching upcoming games", e);
            return RawgGamesResponse.builder().build();
        }
    }

    public RawgGamesResponse getTopRatedGames() {
        log.info("Fetching top-rated games from RAWG");

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/games")
                            .queryParam("key", apiKey)
                            .queryParam("ordering", "-metacritic")
                            .queryParam("metacritic", "80,100")
                            .queryParam("page_size", pageSize)
                            .build())
                    .retrieve()
                    .bodyToMono(RawgGamesResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .doOnSuccess(response -> log.info("Successfully fetched {} top-rated games",
                            response != null && response.getResults() != null ? response.getResults().size() : 0))
                    .doOnError(error -> log.error("Error fetching top-rated games: {}", error.getMessage()))
                    .onErrorResume(e -> {
                        log.warn("Returning empty response due to error");
                        return Mono.just(RawgGamesResponse.builder().build());
                    })
                    .block();
        } catch (Exception e) {
            log.error("Exception while fetching top-rated games", e);
            return RawgGamesResponse.builder().build();
        }
    }
}
