package dormitoryfamily.doomz.domain.follow.controller;

import dormitoryfamily.doomz.domain.follow.service.FollowService;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileListResponseDto;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{memberId}/follows")
    public ResponseEntity<ResponseDto<Void>> saveFollow(
            @PathVariable Long memberId
    ){
        Member loginMember = new Member();
        loginMember.setId(1L);

        followService.saveFollow(loginMember, memberId);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @DeleteMapping("/{memberId}/follows")
    public ResponseEntity<ResponseDto<Void>> cancelFollow(
            @PathVariable Long memberId
    ){
        Member loginMember = new Member();
        loginMember.setId(1L);

        followService.removeFollow(loginMember, memberId);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @GetMapping("/follows")
    public ResponseEntity<ResponseDto<MemberProfileListResponseDto>> getFollowingMembers(){
        Member loginMember = new Member();
        loginMember.setId(1L);

        MemberProfileListResponseDto responseDto = followService.getMyFollowingMemberList(loginMember);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
