package dormitoryfamily.doomz.domain.roomate.lifestyle.repository;

import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.roomate.lifestyle.entity.Lifestyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LifestyleRepository extends JpaRepository<Lifestyle, Long> {

    Optional<Lifestyle> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);

    @Query("SELECT l FROM Lifestyle l JOIN l.member m " +
            "WHERE l.member != :member " +
            "AND m.genderType = :#{#member.genderType} " +
            "AND m.dormitoryType = :#{#member.dormitoryType} " +
            "AND m.isRoommateMatched = false")
    List<Lifestyle> findAllExcludingMember(@Param("member") Member member);

    /**
     * 개발용 API
     * 삭제 예정
     */
    @Modifying
    @Query("DELETE FROM Lifestyle l WHERE l.member = :member")
    void deleteByMember(@Param("member") Member member);
}
