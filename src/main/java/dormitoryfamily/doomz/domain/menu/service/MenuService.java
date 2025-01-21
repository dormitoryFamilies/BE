package dormitoryfamily.doomz.domain.menu.service;

import static dormitoryfamily.doomz.domain.board.article.entity.type.ArticleDormitoryType.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dormitoryfamily.doomz.domain.board.article.entity.type.ArticleDormitoryType;
import dormitoryfamily.doomz.domain.menu.dto.MenuDto;
import dormitoryfamily.doomz.global.exception.ApplicationException;
import dormitoryfamily.doomz.global.exception.ErrorCode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public List<MenuDto> getMenuByDormType(String dormType) {
        ArticleDormitoryType articleDormitoryType = fromName(dormType);
        String key = "menu:" + articleDormitoryType.getName();
        String jsonValue = redisTemplate.opsForValue().get(key);

        if (jsonValue == null) {
            updateMenus();
        }

        try {
            return objectMapper.readValue(jsonValue, new TypeReference<List<MenuDto>>() {});
        } catch (JsonProcessingException e) {
            throw new ApplicationException(ErrorCode.JSON_PARSING_ERROR);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateMenus() {
        try {

            Map<String, List<MenuDto>> newData = new HashMap<>();

            String[] urls = {
                    "https://dorm.chungbuk.ac.kr/home/sub.php?menukey=20041&type=1",
                    "https://dorm.chungbuk.ac.kr/home/sub.php?menukey=20041&type=2",
                    "https://dorm.chungbuk.ac.kr/home/sub.php?menukey=20041&type=3"
            };

            String[] dormTypes = new String[]{
                    MAIN_BUILDING.getName(), // 본관
                    FEMALE_DORMITORY.getName(), //양성재
                    MALE_DORMITORY.getName() // 양진재
            };

            // 모든 데이터를 크롤링하여 임시로 저장
            for (int i = 0; i < urls.length; i++) {
                List<MenuDto> menuList = fetchMenu(urls[i]);
                newData.put("menu:" + dormTypes[i], menuList);
            }

            // 데이터 삭제 및 새로운 데이터 저장
            deleteByPattern("menu:*");
            for (Map.Entry<String, List<MenuDto>> entry : newData.entrySet()) {
                save(entry.getKey(), entry.getValue());
            }

            log.info("Menu update job completed successfully!");
        } catch (Exception e) {
            // 실패 시 로그 출력 및 기존 데이터 유지
            log.error("Error occurred during menu update job. Existing data remains intact.", e);
        }
    }

    private void deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Deleted keys: {}", keys);
        } else {
            log.info("No keys found for pattern: {}", pattern);
        }
    }

    private List<MenuDto> fetchMenu(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new ApplicationException(ErrorCode.MENU_FETCH_ERROR);
        }

        Elements rows = doc.select("table.contTable_c tbody tr");

        List<MenuDto> menuList = new ArrayList<>();
        for (Element row : rows) {
            String dayWithWeekday = row.select("td.foodday").text(); // 예: 월요일 2025-01-13
            String[] dayParts = dayWithWeekday.split(" ", 2);
            String weekday = dayParts[0]; // 예: 월요일
            String day = dayParts.length > 1 ? dayParts[1] : ""; // 예: 2025-01-13

            // 각 식사 데이터
            MenuDto.Meal morning = extractMeal(row.select("td.morning").text());
            MenuDto.Meal lunch = extractMeal(row.select("td.lunch").text());
            MenuDto.Meal dinner = extractMeal(row.select("td.evening").text());

            // 빈 값인 경우 추가하지 않음
            if (!day.isBlank() || morning != null || lunch != null || dinner != null) {
                menuList.add(new MenuDto(day, weekday, morning, lunch, dinner));
            }
        }

        return menuList;
    }

    private MenuDto.Meal extractMeal(String rawText) {
        if (rawText.isBlank()) {
            return null;
        }
        String[] parts = rawText.split(" 에너지:");
        String menu = parts[0].trim(); // 메뉴
        String energy = parts.length > 1 ? parts[1].split(" 단백질:")[0].trim() : ""; // 에너지
        String protein = parts.length > 1 && parts[1].contains("단백질:") ? parts[1].split("단백질:")[1].trim() : ""; // 단백질
        return new MenuDto.Meal(menu, energy, protein);
    }

    private void save(String key, List<MenuDto> menuList) throws Exception {
        String jsonValue = objectMapper.writeValueAsString(menuList);
        redisTemplate.opsForValue().set(key, jsonValue);
        log.info("Saved menu to Redis with key: {}", key);
    }
}
