package dormitoryfamily.doomz.domain.menu.dto;

import java.io.Serializable;

public record MenuDto(
        String day,
        String weekday,
        Meal morning,
        Meal lunch,
        Meal dinner
) implements Serializable {

    public record Meal(
            String menu,
            String energy,
            String protein
    ) implements Serializable {}
}