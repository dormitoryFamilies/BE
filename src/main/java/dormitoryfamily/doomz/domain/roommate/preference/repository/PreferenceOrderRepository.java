package dormitoryfamily.doomz.domain.roommate.preference.repository;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.roommate.preference.entity.PreferenceOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreferenceOrderRepository extends JpaRepository<PreferenceOrder, Long> {

    Boolean existsByMemberId(Long MemberId);

    void deleteByMember(Member member);

    @EntityGraph(attributePaths = "member")
    Optional<PreferenceOrder> findByMember(Member member);
}
