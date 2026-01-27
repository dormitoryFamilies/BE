package dormitoryfamily.doomz.global.chat.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ModerationService {

    private final WebClient webClient;

    public ModerationService(@Value("${openai.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public ModerationResult moderateMessage(String message) {
        try {
            ModerationRequest request = new ModerationRequest("omni-moderation-latest", message);

            ModerationResponse response = webClient.post()
                    .uri("/moderations")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ModerationResponse.class)
                    .block();

            if (response == null || response.results == null || response.results.isEmpty()) {
                return ModerationResult.safe();
            }

            ModerationResponseResult result = response.results.get(0);
            return ModerationResult.from(result);

        } catch (WebClientResponseException.TooManyRequests e) {
            log.error("[ModerationService] Rate limit exceeded (429)");
            return ModerationResult.safe();
        } catch (WebClientResponseException e) {
            log.error("[ModerationService] OpenAI API error: status={}, message={}",
                    e.getStatusCode(), e.getMessage());
            return ModerationResult.safe();
        } catch (Exception e) {
            log.error("[ModerationService] Failed to moderate message: {}", e.getMessage(), e);
            return ModerationResult.safe();
        }
    }

    private record ModerationRequest(String model, String input) {}

    private record ModerationResponse(String id, String model, List<ModerationResponseResult> results) {}

    public record ModerationResponseResult(
            boolean flagged,
            Map<String, Boolean> categories,
            @JsonProperty("category_scores") Map<String, Double> categoryScores
    ) {}
}
