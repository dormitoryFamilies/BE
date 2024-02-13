package dormitoryfamily.doomz;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/")
    public String deployTest(){
        return "deploy Test";
    }
}
