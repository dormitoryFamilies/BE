package dormitoryfamily.doomz.domain.member.follow.controller;

import dormitoryfamily.doomz.domain.member.member.dto.response.MemberProfileListResponseDto;
import dormitoryfamily.doomz.domain.member.member.dto.response.MemberProfilePagingListResponseDto;
import dormitoryfamily.doomz.domain.member.follow.service.FollowService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
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
    public ResponseEntity<ResponseDto<MemberProfilePagingListResponseDto>> findFollowings(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ){
        MemberProfilePagingListResponseDto responseDto = followService.findFollowings(principalDetails, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/followings/search")
    public ResponseEntity<ResponseDto<MemberProfileListResponseDto>> searchFollowings(
            @ModelAttribute @Valid SearchRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        MemberProfileListResponseDto responseDto = followService.searchFollowings(principalDetails, requestDto);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/followers")
    public ResponseEntity<ResponseDto<MemberProfilePagingListResponseDto>> findFollowers(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ){
        MemberProfilePagingListResponseDto responseDto = followService.findFollowers(principalDetails, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/followers/search")
    public ResponseEntity<ResponseDto<MemberProfileListResponseDto>> searchFollowers(
            @ModelAttribute @Valid SearchRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        MemberProfileListResponseDto responseDto = followService.searchFollowers(principalDetails, requestDto);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
