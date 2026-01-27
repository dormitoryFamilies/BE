package dormitoryfamily.doomz.global.chat.moderation;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ModerationResult {

    private boolean flagged;
    private List<String> categories;

    public static ModerationResult safe() {
        return ModerationResult.builder()
                .flagged(false)
                .categories(new ArrayList<>())
                .build();
    }

    public static ModerationResult from(ModerationService.ModerationResponseResult result) {
        List<String> flaggedCategories = new ArrayList<>();

        if (result.flagged()) {
            Map<String, Boolean> categories = result.categories();

            categories.forEach((category, isFlagged) -> {
                if (isFlagged) {
                    flaggedCategories.add(category);
                }
            });
        }

        return ModerationResult.builder()
                .flagged(result.flagged())
                .categories(flaggedCategories)
                .build();
    }
}
