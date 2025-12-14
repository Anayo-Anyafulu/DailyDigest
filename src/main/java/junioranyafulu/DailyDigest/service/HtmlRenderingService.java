package junioranyafulu.DailyDigest.service;

import junioranyafulu.DailyDigest.domain.Digest;
import junioranyafulu.DailyDigest.dto.rawg.RawgGame;
import junioranyafulu.DailyDigest.dto.tmdb.TmdbMovie;
import junioranyafulu.DailyDigest.dto.tmdb.TmdbTVShow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

@Service
@Slf4j
public class HtmlRenderingService {

    private final TemplateEngine templateEngine;
    private final Parser parser;
    private final HtmlRenderer renderer;

    @Autowired
    public HtmlRenderingService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
    }

    public String renderDigestToHtml(
            Digest digest,
            List<RawgGame> games,
            List<TmdbMovie> movies,
            List<TmdbTVShow> tvShows) {
        return renderDigestToHtml(digest, games, movies, tvShows, "all");
    }

    public String renderDigestToHtml(
            Digest digest,
            List<RawgGame> games,
            List<TmdbMovie> movies,
            List<TmdbTVShow> tvShows,
            String edition) {

        log.info("Rendering digest to HTML for date: {} (Edition: {})", digest.getDate(), edition);

        try {
            Context context = new Context();

            // Add digest data
            context.setVariable("digest", digest);
            context.setVariable("edition", edition);

            // Convert Markdown summary to HTML
            if (digest.getSummary() != null) {
                String summaryHtml = renderer.render(parser.parse(digest.getSummary()));
                context.setVariable("summaryHtml", summaryHtml);
            }

            // Filter content based on edition
            boolean showGaming = "all".equalsIgnoreCase(edition) || "gaming".equalsIgnoreCase(edition);
            boolean showMovies = "all".equalsIgnoreCase(edition) || "movies".equalsIgnoreCase(edition);
            boolean showTV = "all".equalsIgnoreCase(edition) || "tv".equalsIgnoreCase(edition);

            // Add section flags
            context.setVariable("gamingSection", showGaming && games != null && !games.isEmpty());
            context.setVariable("moviesSection", showMovies && movies != null && !movies.isEmpty());
            context.setVariable("tvSection", showTV && tvShows != null && !tvShows.isEmpty());

            // Add content lists
            context.setVariable("games", showGaming ? games : List.of());
            context.setVariable("movies", showMovies ? movies : List.of());
            context.setVariable("tvShows", showTV ? tvShows : List.of());

            String html = templateEngine.process("digest-template", context);

            log.info("Successfully rendered HTML digest ({} characters)", html.length());
            return html;

        } catch (Exception e) {
            log.error("Error rendering digest to HTML", e);
            return generateErrorHtml(digest, e);
        }
    }

    private String generateErrorHtml(Digest digest, Exception e) {
        return String.format(
                "<!DOCTYPE html><html><head><title>Error</title></head>" +
                        "<body><h1>Error Generating Digest</h1>" +
                        "<p>Date: %s</p>" +
                        "<p>Error: %s</p></body></html>",
                digest.getDate(),
                e.getMessage());
    }
}
