package junioranyafulu.DailyDigest.client;

import junioranyafulu.DailyDigest.dto.tmdb.TmdbMovieResponse;
import junioranyafulu.DailyDigest.dto.tmdb.TmdbTVResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class TMDbClient {

    private final WebClient webClient;

    @Value("${api.tmdb.key}")
    private String apiKey;

    @Value("${api.tmdb.language}")
    private String language;

    public TMDbClient(@Value("${api.tmdb.base-url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public TmdbMovieResponse getTrendingMovies() {
        log.info("Fetching trending movies from TMDb");

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/trending/movie/day")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbMovieResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .doOnSuccess(response -> log.info("Successfully fetched {} trending movies",
                            response != null && response.getResults() != null ? response.getResults().size() : 0))
                    .doOnError(error -> log.error("Error fetching trending movies: {}", error.getMessage()))
                    .onErrorResume(e -> {
                        log.warn("Returning empty response due to error");
                        return Mono.just(TmdbMovieResponse.builder().build());
                    })
                    .block();
        } catch (Exception e) {
            log.error("Exception while fetching trending movies", e);
            return TmdbMovieResponse.builder().build();
        }
    }

    public TmdbMovieResponse getUpcomingMovies() {
        log.info("Fetching upcoming movies from TMDb");

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/upcoming")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbMovieResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .doOnSuccess(response -> log.info("Successfully fetched {} upcoming movies",
                            response != null && response.getResults() != null ? response.getResults().size() : 0))
                    .doOnError(error -> log.error("Error fetching upcoming movies: {}", error.getMessage()))
                    .onErrorResume(e -> {
                        log.warn("Returning empty response due to error");
                        return Mono.just(TmdbMovieResponse.builder().build());
                    })
                    .block();
        } catch (Exception e) {
            log.error("Exception while fetching upcoming movies", e);
            return TmdbMovieResponse.builder().build();
        }
    }

    public TmdbTVResponse getTrendingTVShows() {
        log.info("Fetching trending TV shows from TMDb");

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/trending/tv/day")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbTVResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .doOnSuccess(response -> log.info("Successfully fetched {} trending TV shows",
                            response != null && response.getResults() != null ? response.getResults().size() : 0))
                    .doOnError(error -> log.error("Error fetching trending TV shows: {}", error.getMessage()))
                    .onErrorResume(e -> {
                        log.warn("Returning empty response due to error");
                        return Mono.just(TmdbTVResponse.builder().build());
                    })
                    .block();
        } catch (Exception e) {
            log.error("Exception while fetching trending TV shows", e);
            return TmdbTVResponse.builder().build();
        }
    }

    public TmdbTVResponse getPopularTVShows() {
        log.info("Fetching popular TV shows from TMDb");

        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/tv/popular")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbTVResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .doOnSuccess(response -> log.info("Successfully fetched {} popular TV shows",
                            response != null && response.getResults() != null ? response.getResults().size() : 0))
                    .doOnError(error -> log.error("Error fetching popular TV shows: {}", error.getMessage()))
                    .onErrorResume(e -> {
                        log.warn("Returning empty response due to error");
                        return Mono.just(TmdbTVResponse.builder().build());
                    })
                    .block();
        } catch (Exception e) {
            log.error("Exception while fetching popular TV shows", e);
            return TmdbTVResponse.builder().build();
        }
    }
}
