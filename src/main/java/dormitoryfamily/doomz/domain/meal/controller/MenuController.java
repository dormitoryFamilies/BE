package dormitoryfamily.doomz.domain.meal.controller;

import dormitoryfamily.doomz.domain.meal.dto.MenuDto;
import dormitoryfamily.doomz.domain.meal.service.MenuService;;
import dormitoryfamily.doomz.global.util.ResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MenuController {

    private final MenuService mealService;

    @GetMapping("/menus")
    public ResponseDto<List<MenuDto>> getMenu(@RequestParam String dormType) {
        return ResponseDto.okWithData(mealService.getMenuByDormType(dormType));
    }
}
