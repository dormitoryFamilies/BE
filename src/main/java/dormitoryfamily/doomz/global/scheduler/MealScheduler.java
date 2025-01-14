package dormitoryfamily.doomz.global.scheduler;

import dormitoryfamily.doomz.domain.meal.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MealScheduler {

    private final MenuService menuService;

    @Scheduled(cron = "0 0 0 * * *") // 매일 00:00:00에 실행
    public void updateMenus() {
        menuService.updateMenus();
    }

}
