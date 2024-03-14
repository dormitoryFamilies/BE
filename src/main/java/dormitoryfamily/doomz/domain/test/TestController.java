package dormitoryfamily.doomz.domain.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/somin")
    public String somin() {
        return "somin zzang!";
    }
}
