package dormitoryfamily.doomz.domain.roomate.repository.mylifestyle;

import dormitoryfamily.doomz.domain.roomate.entity.MyLifestyle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyLifestyleRepository extends JpaRepository<MyLifestyle, Long> {

    boolean existsByMemberId(Long memberId);
}
