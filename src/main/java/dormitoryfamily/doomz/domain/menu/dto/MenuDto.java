package dormitoryfamily.doomz.domain.menu.dto;

import java.io.Serializable;

public record MenuDto(
        String day,        // 날짜 (예: 2025-01-13)
        String weekday,    // 요일 (예: 월요일)
        Meal morning,      // 아침 메뉴
        Meal lunch,        // 점심 메뉴
        Meal dinner        // 저녁 메뉴
) implements Serializable {

    public record Meal(
            String menu,      // 메뉴
            String energy,    // 칼로리 정보
            String protein    // 단백질 정보
    ) implements Serializable {}
}