package dormitoryfamily.doomz.domain.roomate.repository.lifestyle;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.roomate.entity.Lifestyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LifestyleRepository extends JpaRepository<Lifestyle, Long> {

    Optional<Lifestyle> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);

    /**
     * 개발용 API
     * 삭제 예정
     */
    @Modifying
    @Query("DELETE FROM Lifestyle l WHERE l.member = :member")
    void deleteByMember(@Param("member") Member member);
}
