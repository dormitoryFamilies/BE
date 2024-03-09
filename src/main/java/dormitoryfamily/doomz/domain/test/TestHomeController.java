package dormitoryfamily.doomz.domain.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestHomeController {

    @GetMapping("/")
    public String home() {
        log.info("홈 컨트롤러 왔다");
        return "홈 화면입니다!!";
    }

    @GetMapping("/naver")
    public String naver() {
        return "redirect:http://www.naver.com";
    }

    @GetMapping("/somin")
    public String test() {
        log.info("소민 컨트롤러 왔다");
        return "소민짱!!";
    }
}
