package junioranyafulu.DailyDigest.service;

import junioranyafulu.DailyDigest.dto.rawg.RawgGame;
import junioranyafulu.DailyDigest.dto.tmdb.TmdbMovie;
import junioranyafulu.DailyDigest.dto.tmdb.TmdbTVShow;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingService {

    /**
     * Rank movies by a score derived from vote average and vote count.
     * Score = VoteAverage * Log10(VoteCount + 1)
     * This balances high ratings with statistical significance.
     */
    public List<TmdbMovie> rankMovies(List<TmdbMovie> movies) {
        if (movies == null)
            return List.of();
        return movies.stream()
                .sorted(Comparator.comparingDouble(this::calculateMovieScore).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Rank TV shows using the same logic as movies.
     */
    public List<TmdbTVShow> rankTVShows(List<TmdbTVShow> shows) {
        if (shows == null)
            return List.of();
        return shows.stream()
                .sorted(Comparator.comparingDouble(this::calculateTVScore).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Rank games based on Metacritic score (primary) and rating (secondary).
     */
    public List<RawgGame> rankGames(List<RawgGame> games) {
        if (games == null)
            return List.of();
        return games.stream()
                .sorted(Comparator.comparingDouble(this::calculateGameScore).reversed())
                .collect(Collectors.toList());
    }

    private double calculateMovieScore(TmdbMovie movie) {
        if (movie.getVoteAverage() == null || movie.getVoteCount() == null)
            return 0.0;
        // Logarithmic scaling for vote count to prevent popularity from completely
        // dominating quality
        return movie.getVoteAverage() * Math.log10(movie.getVoteCount() + 1);
    }

    private double calculateTVScore(TmdbTVShow show) {
        if (show.getVoteAverage() == null || show.getVoteCount() == null)
            return 0.0;
        return show.getVoteAverage() * Math.log10(show.getVoteCount() + 1);
    }

    private double calculateGameScore(RawgGame game) {
        double score = 0.0;
        // Metacritic is a strong signal (0-100)
        if (game.getMetacritic() != null) {
            score += game.getMetacritic();
        }
        // User rating (0-5) scaled to 0-20 equivalent
        if (game.getRating() != null) {
            score += game.getRating() * 4; // Give user rating some weight
        }
        return score;
    }
}
