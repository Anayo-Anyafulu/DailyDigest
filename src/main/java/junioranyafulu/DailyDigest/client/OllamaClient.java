package junioranyafulu.DailyDigest.client;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import junioranyafulu.DailyDigest.dto.Article;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

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

        promptBuilder.append("You are an expert entertainment editor.\n")
                .append("\n")
                .append("Your task is to generate a concise, engaging Daily Entertainment News Brief based on the provided raw news data.\n")
                .append("\n")
                .append("Content domains:\n")
                .append("- Gaming\n")
                .append("- Movies\n")
                .append("- TV Series / Streaming\n")
                .append("\n")
                .append("Instructions:\n")
                .append("1. Summarize the most important news only (skip clickbait).\n")
                .append("2. Write in a professional, magazine-style tone (IGN / Variety level).\n")
                .append("3. Be factual, neutral, and clear.\n")
                .append("4. Avoid emojis, slang, or hype.\n")
                .append("5. Keep each section easy to scan.\n")
                .append("\n")
                .append("Output structure (STRICT):\n")
                .append("- Title (1 line)\n")
                .append("- Date\n")
                .append("- Gaming Highlights (3–5 bullet points)\n")
                .append("- Movie Highlights (3–5 bullet points)\n")
                .append("- TV / Series Highlights (3–5 bullet points)\n")
                .append("- \"Worth Your Time Today\" (1–2 short recommendations)\n")
                .append("\n")
                .append("Rules:\n")
                .append("- Each bullet point: max 2 sentences.\n")
                .append("- Do NOT invent facts.\n")
                .append("- Do NOT mention sources unless provided.\n")
                .append("- Prefer clarity over length.\n")
                .append("\n")
                .append("The output will be embedded into a dynamically generated HTML newsletter.\n")
                .append("\n")
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
