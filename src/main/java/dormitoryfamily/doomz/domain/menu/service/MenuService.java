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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 기숙사 타입에 따른 메뉴 정보를 반환
     */
    public List<MenuDto> getMenuByDormType(String dormType) {
        ArticleDormitoryType articleDormitoryType = fromName(dormType);
        String key = "menu:" + articleDormitoryType.getName();
        String jsonValue = redisTemplate.opsForValue().get(key);

        if (jsonValue == null) {
            log.warn("메뉴 캐시 없음. 업데이트 실행: {}", dormType);
            updateMenusAsync(); // 캐시 없으면 비동기 업데이트
            return Collections.emptyList(); // 임시로 빈 리스트 반환
        }

        try {
            return objectMapper.readValue(jsonValue, new TypeReference<List<MenuDto>>() {});
        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 오류: {}", key, e);
            throw new ApplicationException(ErrorCode.JSON_PARSING_ERROR);
        }
    }

    /**
     * 매일 자정에 비동기 크롤링 실행
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateMenusAsync() {

        String[] urls = {
                "https://dorm.chungbuk.ac.kr/home/sub.php?menukey=20041&type=1",
                "https://dorm.chungbuk.ac.kr/home/sub.php?menukey=20041&type=2",
                "https://dorm.chungbuk.ac.kr/home/sub.php?menukey=20041&type=3"
        };

        String[] dormTypes = new String[]{
                MAIN_BUILDING.getName(),
                FEMALE_DORMITORY.getName(),
                MALE_DORMITORY.getName()
        };

        List<CompletableFuture<Map.Entry<String, List<MenuDto>>>> futureList = new ArrayList<>();

        // 비동기 크롤링 실행
        for (int i = 0; i < urls.length; i++) {
            String key = "menu:" + dormTypes[i];
            futureList.add(fetchMenuAsync(urls[i]).thenApply(menuList -> new AbstractMap.SimpleEntry<>(key, menuList)));
        }

        // 모든 크롤링 완료될 때까지 대기
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]))
                .thenApply(v -> futureList.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                )
                .thenAccept(this::saveMenus)
                .exceptionally(e -> {
                    log.error("메뉴 업데이트 중 오류 발생!", e);
                    return null;
                });
    }

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

    /**
     * 메뉴 데이터를 Redis에 저장
     */
    private void saveMenus(Map<String, List<MenuDto>> menuData) {
        // 기존 데이터 삭제 후 저장
        deleteByPattern("menu:*");
        menuData.forEach(this::save);
        log.info("메뉴 업데이트 완료!");
    }

    /**
     * Redis에서 특정 패턴의 키 삭제
     */
    private void deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("삭제된 캐시 키: {}", keys);
        } else {
            log.info("삭제할 캐시 없음: {}", pattern);
        }
    }

    /**
     * Redis에 메뉴 저장
     */
    private void save(String key, List<MenuDto> menuList) {
        try {
            String jsonValue = objectMapper.writeValueAsString(menuList);
            redisTemplate.opsForValue().set(key, jsonValue);
            log.info("Redis 저장 완료: {}", key);
        } catch (JsonProcessingException e) {
            log.error("edis 저장 실패: {}", key, e);
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
