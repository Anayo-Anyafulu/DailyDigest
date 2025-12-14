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

@Service
@Slf4j
public class HtmlRenderingService {

    private final TemplateEngine templateEngine;

    @Autowired
    public HtmlRenderingService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String renderDigestToHtml(
            Digest digest,
            List<RawgGame> games,
            List<TmdbMovie> movies,
            List<TmdbTVShow> tvShows) {

        log.info("Rendering digest to HTML for date: {}", digest.getDate());

        try {
            Context context = new Context();

            // Add digest data
            context.setVariable("digest", digest);

            // Add section flags
            context.setVariable("gamingSection", games != null && !games.isEmpty());
            context.setVariable("moviesSection", movies != null && !movies.isEmpty());
            context.setVariable("tvSection", tvShows != null && !tvShows.isEmpty());

            // Add content lists
            context.setVariable("games", games);
            context.setVariable("movies", movies);
            context.setVariable("tvShows", tvShows);

            // TODO: Add recommendations (could be extracted from AI summary or manually
            // curated)
            context.setVariable("recommendations", null);

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
