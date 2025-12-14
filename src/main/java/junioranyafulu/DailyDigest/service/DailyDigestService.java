package junioranyafulu.DailyDigest.service;

import junioranyafulu.DailyDigest.client.NewsApiClient;
import junioranyafulu.DailyDigest.client.OllamaClient;
import junioranyafulu.DailyDigest.client.TMDbClient;
import junioranyafulu.DailyDigest.client.RAWGClient;
import junioranyafulu.DailyDigest.domain.Digest;
import junioranyafulu.DailyDigest.domain.DigestStatus;
import junioranyafulu.DailyDigest.dto.NewsApiResponse;
import junioranyafulu.DailyDigest.dto.rawg.RawgGamesResponse;
import junioranyafulu.DailyDigest.dto.tmdb.TmdbMovieResponse;
import junioranyafulu.DailyDigest.dto.tmdb.TmdbTVResponse;
import junioranyafulu.DailyDigest.repository.DigestRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DailyDigestService {

    private final NewsApiClient newsApiClient;
    private final OllamaClient ollamaClient;
    private final TMDbClient tmdbClient;
    private final RAWGClient rawgClient;
    private final DigestRepository digestRepository;
    private final HtmlRenderingService htmlRenderingService;

    private final RankingService rankingService;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Autowired
    public DailyDigestService(
            NewsApiClient newsApiClient,
            OllamaClient ollamaClient,
            TMDbClient tmdbClient,
            RAWGClient rawgClient,
            DigestRepository digestRepository,
            HtmlRenderingService htmlRenderingService,
            RankingService rankingService,
            com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        this.newsApiClient = newsApiClient;
        this.ollamaClient = ollamaClient;
        this.tmdbClient = tmdbClient;
        this.rawgClient = rawgClient;
        this.digestRepository = digestRepository;
        this.htmlRenderingService = htmlRenderingService;
        this.rankingService = rankingService;
        this.objectMapper = objectMapper;
    }

    public Digest generateDailyDigest() {
        log.info("Starting daily digest generation");

        try {
            // Create a new digest
            Digest digest = Digest.builder()
                    .date(LocalDate.now())
                    .title("Daily Entertainment Digest - " + LocalDate.now())
                    .status(DigestStatus.GENERATING)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            // Save initial digest
            @SuppressWarnings("null")
            Digest savedDigest = digestRepository.save(digest);
            digest = savedDigest;
            log.info("Created digest with ID: {}", digest.getId());

            // Fetch data from all sources asynchronously
            log.info("Fetching data from all sources in parallel...");

            CompletableFuture<NewsApiResponse> newsFuture = CompletableFuture.supplyAsync(() -> {
                log.info("Fetching entertainment news from NewsAPI...");
                return newsApiClient.getTopHeadlines();
            });

            CompletableFuture<TmdbMovieResponse> moviesFuture = CompletableFuture.supplyAsync(() -> {
                log.info("Fetching trending movies from TMDb...");
                return tmdbClient.getTrendingMovies();
            });

            CompletableFuture<TmdbTVResponse> tvFuture = CompletableFuture.supplyAsync(() -> {
                log.info("Fetching trending TV shows from TMDb...");
                return tmdbClient.getTrendingTVShows();
            });

            CompletableFuture<RawgGamesResponse> gamesFuture = CompletableFuture.supplyAsync(() -> {
                log.info("Fetching trending games from RAWG...");
                return rawgClient.getTrendingGames();
            });

            // Wait for all to complete
            CompletableFuture.allOf(newsFuture, moviesFuture, tvFuture, gamesFuture).join();

            NewsApiResponse newsResponse = newsFuture.get();
            TmdbMovieResponse moviesResponse = moviesFuture.get();
            TmdbTVResponse tvResponse = tvFuture.get();
            RawgGamesResponse gamesResponse = gamesFuture.get();

            // Generate AI summary
            if (newsResponse != null && newsResponse.getArticles() != null && !newsResponse.getArticles().isEmpty()) {
                log.info("Generating AI summary for {} articles", newsResponse.getArticles().size());
                String aiSummary = ollamaClient.generateSummary(newsResponse.getArticles());
                digest.setSummary(aiSummary);
                log.info("AI summary generated successfully");
            } else {
                log.warn("No articles found to summarize");
                digest.setSummary("No entertainment news available for today.");
            }

            // Generate HTML content
            log.info("Rendering HTML digest...");

            // Rank content
            var rankedGames = rankingService
                    .rankGames(gamesResponse != null ? gamesResponse.getResults() : Collections.emptyList());
            var rankedMovies = rankingService
                    .rankMovies(moviesResponse != null ? moviesResponse.getResults() : Collections.emptyList());
            var rankedTV = rankingService
                    .rankTVShows(tvResponse != null ? tvResponse.getResults() : Collections.emptyList());

            // Save raw data for dynamic rendering
            try {
                digest.setRawGames(objectMapper.writeValueAsString(rankedGames));
                digest.setRawMovies(objectMapper.writeValueAsString(rankedMovies));
                digest.setRawTV(objectMapper.writeValueAsString(rankedTV));
            } catch (Exception e) {
                log.error("Failed to serialize raw data", e);
            }

            String htmlContent = htmlRenderingService.renderDigestToHtml(
                    digest,
                    rankedGames,
                    rankedMovies,
                    rankedTV);
            digest.setHtmlContent(htmlContent);

            // Update digest status
            digest.setStatus(DigestStatus.COMPLETED);
            digest.setUpdatedAt(LocalDateTime.now());

            // Save final digest
            digest = java.util.Objects.requireNonNull(digestRepository.save(digest));
            log.info("Daily digest generated successfully with ID: {}", digest.getId());

            return digest;

        } catch (Exception e) {
            log.error("Error generating daily digest", e);
            throw new RuntimeException("Failed to generate daily digest", e);
        }
    }

    public Digest getLatestDigest() {
        return digestRepository.findLatestCompleted()
                .orElseThrow(() -> new RuntimeException("No completed digest found"));
    }

    public String getDigestHtml(Digest digest, String edition) {
        try {
            List<junioranyafulu.DailyDigest.dto.rawg.RawgGame> games = Collections.emptyList();
            List<junioranyafulu.DailyDigest.dto.tmdb.TmdbMovie> movies = Collections.emptyList();
            List<junioranyafulu.DailyDigest.dto.tmdb.TmdbTVShow> tvShows = Collections.emptyList();

            if (digest.getRawGames() != null) {
                games = objectMapper.readValue(digest.getRawGames(),
                        new com.fasterxml.jackson.core.type.TypeReference<List<junioranyafulu.DailyDigest.dto.rawg.RawgGame>>() {
                        });
            }
            if (digest.getRawMovies() != null) {
                movies = objectMapper.readValue(digest.getRawMovies(),
                        new com.fasterxml.jackson.core.type.TypeReference<List<junioranyafulu.DailyDigest.dto.tmdb.TmdbMovie>>() {
                        });
            }
            if (digest.getRawTV() != null) {
                tvShows = objectMapper.readValue(digest.getRawTV(),
                        new com.fasterxml.jackson.core.type.TypeReference<List<junioranyafulu.DailyDigest.dto.tmdb.TmdbTVShow>>() {
                        });
            }

            return htmlRenderingService.renderDigestToHtml(digest, games, movies, tvShows, edition);
        } catch (Exception e) {
            log.error("Error deserializing raw data for digest {}", digest.getId(), e);
            // Fallback to stored HTML if deserialization fails
            return digest.getHtmlContent();
        }
    }

    public Digest getDigestByDate(LocalDate date) {
        return digestRepository.findByDate(date)
                .orElseThrow(() -> new RuntimeException("No digest found for date: " + date));
    }
}
