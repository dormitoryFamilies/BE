package dormitoryfamily.doomz.domain.matching.controller;

import dormitoryfamily.doomz.domain.matching.service.MatchingRequestService;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfilePagingListResponseDto;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MatchingRequestController {

    private final MatchingRequestService matchingRequestService;

    @PostMapping("/members/{memberId}/matching-requests")
    public ResponseEntity<ResponseDto<Void>> saveMatchingRequest(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        matchingRequestService.saveMatchingRequest(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.created());
    }


    @DeleteMapping("/members/{memberId}/matching-requests")
    public ResponseEntity<ResponseDto<Void>> cancelMatchingRequest(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        matchingRequestService.deleteMatchingRequest(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/my/matching-requests")
    public ResponseEntity<ResponseDto<MemberProfilePagingListResponseDto>> findMyMatchingRequest(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam String status,
            Pageable pageable
    ){
        MemberProfilePagingListResponseDto responseDto = matchingRequestService.findMyMatchingRequest(principalDetails, status, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}

