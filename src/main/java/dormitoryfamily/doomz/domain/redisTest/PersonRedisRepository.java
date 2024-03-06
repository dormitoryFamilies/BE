package dormitoryfamily.doomz.domain.redisTest;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories
public interface PersonRedisRepository extends CrudRepository<Person, String> {
}