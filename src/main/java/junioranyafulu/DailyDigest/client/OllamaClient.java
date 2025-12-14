package junioranyafulu.DailyDigest.client;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import junioranyafulu.DailyDigest.dto.Article;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class OllamaClient {

    private final ChatModel chatModel;

    @Autowired
    public OllamaClient(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generateSummary(final List<Article> articles) {
        log.info("Generating summary for {} articles using Ollama", articles.size());

        final String promptText = buildPrompt(articles);

        try {
            Prompt prompt = new Prompt(promptText);
            String response = chatModel.call(prompt).getResult().getOutput().getContent();

            log.info("Successfully generated summary");
            return response;
        } catch (Exception e) {
            log.error("Error generating summary with Ollama", e);
            return "Error generating summary: " + e.getMessage();
        }
    }

    private String buildPrompt(List<Article> articles) {
        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append("You are a senior entertainment editor for a premium digital magazine.\n\n")
                .append("Your task is to transform raw entertainment data into a\n")
                .append("clean, high-signal Daily Entertainment Digest.\n\n")
                .append("Content domains:\n")
                .append("- Gaming\n")
                .append("- Movies\n")
                .append("- TV / Streaming\n\n")
                .append("STRICT GUIDELINES:\n")
                .append("1. **Relevance**: Only include news directly relevant to entertainment.\n")
                .append("2. **Importance**: Prioritize major announcements over minor updates.\n")
                .append("3. **Clarity**: Remove duplication and conflicting dates.\n")
                .append("4. **Tone**: Professional, neutral, and sophisticated (Variety / IGN style).\n")
                .append("5. **No Sources**: Do not mention data sources (e.g., 'According to...') in the text.\n")
                .append("6. **Omission**: If a section has no strong news, omit it entirely.\n\n")
                .append("OUTPUT STRUCTURE (STRICT):\n\n")
                .append("OUTPUT STRUCTURE (STRICT):\n\n")
                .append("## Gaming News\n")
                .append("- **[Topic/Game Name]**: Concise summary of the development (max 2 sentences).\n")
                .append("- **[Topic/Game Name]**: Concise summary of the development (max 2 sentences).\n\n")
                .append("## Anticipated Game Trailers / Releases\n")
                .append("- **[Game Name]**: Expected window (Month/Quarter) or Trailer date. One sentence on why it's hyped.\n\n")
                .append("## Movie News\n")
                .append("- **[Movie/Topic]**: Concise summary of casting, production, or box office news.\n\n")
                .append("## Anticipated Movies / Trailers\n")
                .append("- **[Movie Name]**: Expected window. Brief reason for anticipation (director, cast, buzz).\n\n")
                .append("## TV / Streaming News\n")
                .append("- **[Show/Platform]**: Concise summary of renewals, cancellations, or premiere dates.\n\n")
                .append("## Anticipated Series / Trailers\n")
                .append("- **[Series Name]**: Expected premiere/trailer. One sentence explaining audience interest.\n\n")
                .append("## Worth Your Time Today\n")
                .append("- **[Recommendation]**: A confident, curated pick. One sentence explaining the appeal.\n\n")
                .append("STYLE RULES:\n")
                .append("- **Bold** the key subject at the start of each bullet (as shown above).\n")
                .append("- No emojis.\n")
                .append("- No clickbait.\n")
                .append("- No speculation beyond widely reported expectations.\n")
                .append("- Clear, scannable formatting.\n\n")
                .append("The output must read like a curated digital magazine brief.\n\n")
                .append("Here are the news articles:\n\n");

        for (Article article : articles) {
            promptBuilder
                    .append("Title: ").append(article.getTitle()).append("\n")
                    .append("Description: ").append(article.getDescription() != null ? article.getDescription() : "N/A")
                    .append("\n")
                    .append("Published: ").append(article.getPublishedAt()).append("\n")
                    .append("URL: ").append(article.getUrl()).append("\n\n");
        }

        return promptBuilder.toString();
    }
}
