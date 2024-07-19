package dormitoryfamily.doomz.domain.roomate.repository.preferencelifestyle;

import dormitoryfamily.doomz.domain.roomate.entity.PreferenceLifestyle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<PreferenceLifestyle, Long> {

    Boolean existsByMemberId(Long MemberId);
}
