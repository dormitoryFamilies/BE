package dormitoryfamily.doomz.domain.menu.service;

import dormitoryfamily.doomz.domain.menu.dto.MenuDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MenuCrawler {

    /**
     * 비동기 크롤링 메서드
     */
    @Async
    public CompletableFuture<List<MenuDto>> fetchMenuAsync(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements rows = doc.select("table.contTable_c tbody tr");

            List<MenuDto> menuList = new ArrayList<>();
            for (Element row : rows) {
                String dayWithWeekday = row.select("td.foodday").text();
                String[] dayParts = dayWithWeekday.split(" ", 2);
                String weekday = dayParts[0];
                String day = dayParts.length > 1 ? dayParts[1] : "";

                MenuDto.Meal morning = extractMeal(row.select("td.morning").text());
                MenuDto.Meal lunch = extractMeal(row.select("td.lunch").text());
                MenuDto.Meal dinner = extractMeal(row.select("td.evening").text());

                if (!day.isBlank() || morning != null || lunch != null || dinner != null) {
                    menuList.add(new MenuDto(day, weekday, morning, lunch, dinner));
                }
            }
            return CompletableFuture.completedFuture(menuList);
        } catch (IOException e) {
            log.error("크롤링 실패: {}", url, e);
            return CompletableFuture.completedFuture(Collections.emptyList()); // 실패 시 빈 리스트 반환
        }
    }

    private MenuDto.Meal extractMeal(String rawText) {
        if (rawText.isBlank()) {
            return null;
        }
        String[] parts = rawText.split(" 에너지:");
        String menu = parts[0].trim();
        String energy = parts.length > 1 ? parts[1].split(" 단백질:")[0].trim() : "";
        String protein = parts.length > 1 && parts[1].contains("단백질:") ? parts[1].split("단백질:")[1].trim() : "";
        return new MenuDto.Meal(menu, energy, protein);
    }

}
