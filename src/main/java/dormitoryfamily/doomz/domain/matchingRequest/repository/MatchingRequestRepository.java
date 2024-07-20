package dormitoryfamily.doomz.domain.matchingRequest.repository;

import dormitoryfamily.doomz.domain.matchingRequest.entity.MatchingRequest;
import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;


public interface MatchingRequestRepository extends JpaRepository<MatchingRequest, Long> {

    boolean existsBySenderAndReceiver(Member Sender, Member Receiver);

    Optional<MatchingRequest> findBySenderAndReceiver(Member sender, Member receiver);
}
