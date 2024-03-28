package dormitoryfamily.doomz.global.jwt.refresh.repository;

import dormitoryfamily.doomz.global.jwt.refresh.entity.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, Long> {

    Boolean existsByEmail(String email);

    Optional<RefreshTokenEntity> findByEmail(String email);
}
