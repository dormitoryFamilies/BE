package dormitoryfamily.doomz.domain.redisTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {

    @Autowired
    PersonRedisRepository repository;

    @PostMapping("/redis/person")
    public void test(){
        Person person = new Person("park", 10);
        repository.save(person);
    }

    @GetMapping("/deploy/test")
    public String test2(){
        return "success";
    }
}
