package dormitoryfamily.doomz.domain.roomate.repository.preference;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.roomate.entity.PreferenceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreferenceOrderRepository extends JpaRepository<PreferenceOrder, Long> {

    Boolean existsByMemberId(Long MemberId);

    void deleteByMember(Member member);

    Optional<PreferenceOrder> findByMember(Member member);
}
