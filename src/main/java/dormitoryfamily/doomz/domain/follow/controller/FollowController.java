package dormitoryfamily.doomz.domain.follow.controller;

import dormitoryfamily.doomz.domain.follow.service.FollowService;
import dormitoryfamily.doomz.domain.member.dto.request.MemberSearchRequestDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileListResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfilePagingListResponseDto;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/followings")
    public ResponseEntity<ResponseDto<MemberProfilePagingListResponseDto>> getFollowingMembers(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ){
        MemberProfilePagingListResponseDto responseDto = followService.getMyFollowingMemberList(principalDetails, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/followings/search")
    public ResponseEntity<ResponseDto<MemberProfileListResponseDto>> searchFollowings(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @ModelAttribute @Valid MemberSearchRequestDto requestDto
    ){
        MemberProfileListResponseDto responseDto = followService.searchFollowings(principalDetails, requestDto);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/followers")
    public ResponseEntity<ResponseDto<MemberProfilePagingListResponseDto>> getFollowerMembers(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ){
        MemberProfilePagingListResponseDto responseDto = followService.getMyFollowerMemberList(principalDetails, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/followers/search")
    public ResponseEntity<ResponseDto<MemberProfileListResponseDto>> searchFollowers(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @ModelAttribute @Valid MemberSearchRequestDto requestDto
    ){
        MemberProfileListResponseDto responseDto = followService.searchFollowers(principalDetails, requestDto);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
