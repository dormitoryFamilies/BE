package dormitoryfamily.doomz.domain.roomate.repository.preferenceorder;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.roomate.entity.PreferenceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreferenceOrderRepository extends JpaRepository<PreferenceOrder, Long> {

    Boolean existsByMemberId(Long MemberId);

    void deleteByMember(Member member);

    List<PreferenceOrder> findAllByMemberOrderByPreferenceOrderAsc(Member member);

    @Query("SELECT p FROM PreferenceOrder p WHERE p.member = :member AND p.preferenceOrder = :preferenceOrder")
    PreferenceOrder findByMemberAndPreferenceOrder(Member member, Integer preferenceOrder);
}
