package dormitoryfamily.doomz.domain.matchingResult.service;

import dormitoryfamily.doomz.domain.matchingRequest.repository.MatchingRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingResultService {

    private final MatchingRequestRepository matchingRequestRepository;
}
