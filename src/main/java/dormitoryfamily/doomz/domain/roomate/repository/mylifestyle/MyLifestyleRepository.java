package dormitoryfamily.doomz.domain.roomate.repository.mylifestyle;

import dormitoryfamily.doomz.domain.roomate.entity.MyLifestyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyLifestyleRepository extends JpaRepository<MyLifestyle, Long> {

    Optional<MyLifestyle> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
