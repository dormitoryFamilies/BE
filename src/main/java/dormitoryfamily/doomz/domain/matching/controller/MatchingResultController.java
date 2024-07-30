package dormitoryfamily.doomz.domain.matching.controller;

import dormitoryfamily.doomz.domain.matching.service.MatchingResultService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MatchingResultController {

    private final MatchingResultService matchingResultService;

    @PostMapping("/{memberId}/matching-results")
    public ResponseEntity<ResponseDto<Void>> saveMatchingResult(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        matchingResultService.saveMatchingResult(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @DeleteMapping("/{memberId}/matching-results")
    public ResponseEntity<ResponseDto<Void>> cancelMatchingResult(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        matchingResultService.cancelMatchingResult(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.ok());
    }
}