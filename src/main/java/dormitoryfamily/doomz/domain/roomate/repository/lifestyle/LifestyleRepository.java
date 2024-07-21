package dormitoryfamily.doomz.domain.roomate.repository.lifestyle;

import dormitoryfamily.doomz.domain.roomate.entity.Lifestyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LifestyleRepository extends JpaRepository<Lifestyle, Long> {

    Optional<Lifestyle> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
