package dormitoryfamily.doomz.domain.follow.controller;

import dormitoryfamily.doomz.domain.follow.service.FollowService;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileListResponseDto;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{memberId}/follows")
    public ResponseEntity<ResponseDto<Void>> saveFollow(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        followService.saveFollow(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @DeleteMapping("/{memberId}/follows")
    public ResponseEntity<ResponseDto<Void>> cancelFollow(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        followService.removeFollow(principalDetails, memberId);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @GetMapping("/followings")
    public ResponseEntity<ResponseDto<MemberProfileListResponseDto>> getFollowingMembers(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        MemberProfileListResponseDto responseDto = followService.getMyFollowingMemberList(principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/followers")
    public ResponseEntity<ResponseDto<MemberProfileListResponseDto>> getFollowerMembers(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        MemberProfileListResponseDto responseDto = followService.getMyFollowerMemberList(principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
