package dormitoryfamily.doomz.global.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class OpenAIClient {

    private static final String BASE_URL = "https://api.openai.com/v1";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final WebClient webClient;
    private final String apiKey;

    public OpenAIClient(@Value("${openai.api.key:}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    public Optional<String> chatCompletion(String systemPrompt, String userPrompt) {
        if (isApiKeyMissing()) {
            return Optional.empty();
        }

        try {
            var request = new ChatRequest(
                    "gpt-4o-mini",
                    List.of(
                            new ChatMessage("system", systemPrompt),
                            new ChatMessage("user", userPrompt)
                    ),
                    0.7,
                    200
            );

            var response = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .timeout(TIMEOUT)
                    .block();

            if (response == null || response.choices == null || response.choices.isEmpty()) {
                return Optional.empty();
            }

            return Optional.ofNullable(response.choices.get(0).message.content);

        } catch (Exception e) {
            log.error("Chat Completion API 호출 실패: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public ModerationResult moderate(String text) {
        if (isApiKeyMissing()) {
            return ModerationResult.safe();
        }

        try {
            var request = new ModerationRequest("omni-moderation-latest", text);

            var response = webClient.post()
                    .uri("/moderations")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ModerationResponse.class)
                    .timeout(TIMEOUT)
                    .block();

            if (response == null || response.results == null || response.results.isEmpty()) {
                return ModerationResult.safe();
            }

            return ModerationResult.from(response.results.get(0));

        } catch (WebClientResponseException.TooManyRequests e) {
            log.error("Moderation API rate limit 초과");
            return ModerationResult.safe();
        } catch (Exception e) {
            log.error("Moderation API 호출 실패: {}", e.getMessage());
            return ModerationResult.safe();
        }
    }

    private boolean isApiKeyMissing() {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("OpenAI API 키가 설정되지 않았습니다.");
            return true;
        }
        return false;
    }

    // Chat Completion DTOs
    private record ChatRequest(String model, List<ChatMessage> messages, double temperature, int max_tokens) {}
    private record ChatMessage(String role, String content) {}
    private record ChatResponse(List<Choice> choices) {
        record Choice(Message message) {
            record Message(String content) {}
        }
    }

    // Moderation DTOs
    private record ModerationRequest(String model, String input) {}
    private record ModerationResponse(List<ModerationResponseResult> results) {}

    public record ModerationResponseResult(
            boolean flagged,
            Map<String, Boolean> categories,
            @JsonProperty("category_scores") Map<String, Double> categoryScores
    ) {}

    public record ModerationResult(boolean flagged, List<String> categories) {
        public static ModerationResult safe() {
            return new ModerationResult(false, List.of());
        }

        public static ModerationResult from(ModerationResponseResult result) {
            if (!result.flagged()) {
                return safe();
            }
            List<String> flaggedCategories = result.categories().entrySet().stream()
                    .filter(Map.Entry::getValue)
                    .map(Map.Entry::getKey)
                    .toList();
            return new ModerationResult(true, flaggedCategories);
        }
    }
}
