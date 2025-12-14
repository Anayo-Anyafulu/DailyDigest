package junioranyafulu.DailyDigest.client;

import junioranyafulu.DailyDigest.dto.tmdb.TmdbMovieResponse;
import junioranyafulu.DailyDigest.dto.tmdb.TmdbTVResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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
                .baseUrl(java.util.Objects.requireNonNull(baseUrl))
                .build();
    }

    @Cacheable("movies")
    public TmdbMovieResponse getTrendingMovies() {
        log.info("Fetching trending movies from TMDb");

        try {
            TmdbMovieResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/trending/movie/day")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbMovieResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            if (response != null && response.getResults() != null) {
                enrichMovies(response.getResults());
            }

            return response;
        } catch (Exception e) {
            log.error("Exception while fetching trending movies", e);
            return TmdbMovieResponse.builder().build();
        }
    }

    @Cacheable("tv_shows")
    public TmdbTVResponse getTrendingTVShows() {
        log.info("Fetching trending TV shows from TMDb");

        try {
            TmdbTVResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/trending/tv/day")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbTVResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            if (response != null && response.getResults() != null) {
                enrichTVShows(response.getResults());
            }

            return response;
        } catch (Exception e) {
            log.error("Exception while fetching trending TV shows", e);
            return TmdbTVResponse.builder().build();
        }
    }

    private void enrichMovies(java.util.List<junioranyafulu.DailyDigest.dto.tmdb.TmdbMovie> movies) {
        // Limit to top 5 to avoid rate limits and improve performance
        movies.stream().limit(5).parallel().forEach(movie -> {
            try {
                junioranyafulu.DailyDigest.dto.tmdb.TmdbDetailsResponse details = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/movie/" + movie.getId())
                                .queryParam("api_key", apiKey)
                                .queryParam("append_to_response", "videos,credits")
                                .build())
                        .retrieve()
                        .bodyToMono(junioranyafulu.DailyDigest.dto.tmdb.TmdbDetailsResponse.class)
                        .timeout(Duration.ofSeconds(5))
                        .onErrorResume(e -> Mono.empty())
                        .block();

                if (details != null) {
                    // Extract trailer
                    if (details.getVideos() != null && details.getVideos().getResults() != null) {
                        details.getVideos().getResults().stream()
                                .filter(v -> "YouTube".equals(v.getSite()) && "Trailer".equals(v.getType()))
                                .findFirst()
                                .ifPresent(v -> movie.setTrailerKey(v.getKey()));
                    }

                    // Extract cast (top 3)
                    if (details.getCredits() != null && details.getCredits().getCast() != null) {
                        java.util.List<String> castNames = details.getCredits().getCast().stream()
                                .limit(3)
                                .map(junioranyafulu.DailyDigest.dto.tmdb.TmdbCreditsResponse.TmdbCast::getName)
                                .collect(java.util.stream.Collectors.toList());
                        movie.setCast(castNames);
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to enrich movie {}: {}", movie.getId(), e.getMessage());
            }
        });
    }

    private void enrichTVShows(java.util.List<junioranyafulu.DailyDigest.dto.tmdb.TmdbTVShow> shows) {
        // Limit to top 5
        shows.stream().limit(5).parallel().forEach(show -> {
            try {
                junioranyafulu.DailyDigest.dto.tmdb.TmdbDetailsResponse details = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/tv/" + show.getId())
                                .queryParam("api_key", apiKey)
                                .queryParam("append_to_response", "videos,credits")
                                .build())
                        .retrieve()
                        .bodyToMono(junioranyafulu.DailyDigest.dto.tmdb.TmdbDetailsResponse.class)
                        .timeout(Duration.ofSeconds(5))
                        .onErrorResume(e -> Mono.empty())
                        .block();

                if (details != null) {
                    // Extract trailer
                    if (details.getVideos() != null && details.getVideos().getResults() != null) {
                        details.getVideos().getResults().stream()
                                .filter(v -> "YouTube".equals(v.getSite()) && "Trailer".equals(v.getType()))
                                .findFirst()
                                .ifPresent(v -> show.setTrailerKey(v.getKey()));
                    }

                    // Extract cast (top 3)
                    if (details.getCredits() != null && details.getCredits().getCast() != null) {
                        java.util.List<String> castNames = details.getCredits().getCast().stream()
                                .limit(3)
                                .map(junioranyafulu.DailyDigest.dto.tmdb.TmdbCreditsResponse.TmdbCast::getName)
                                .collect(java.util.stream.Collectors.toList());
                        show.setCast(castNames);
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to enrich TV show {}: {}", show.getId(), e.getMessage());
            }
        });
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
                    .block();
        } catch (Exception e) {
            log.error("Exception while fetching upcoming movies", e);
            return TmdbMovieResponse.builder().build();
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
                    .block();
        } catch (Exception e) {
            log.error("Exception while fetching popular TV shows", e);
            return TmdbTVResponse.builder().build();
        }
    }
}
