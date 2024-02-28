package dormitoryfamily.doomz.domain.follow.controller;

import dormitoryfamily.doomz.domain.follow.service.FollowService;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow/{memberId}")
public class FollowController {

    private final FollowService followService;

    @PostMapping()
    public ResponseEntity<ResponseDto<Void>> saveFollow(
            @PathVariable Long memberId
    ){
        Member loginMember = new Member();
        loginMember.setId(1L);

        followService.saveFollow(loginMember, memberId);
        return ResponseEntity.ok(ResponseDto.created());
    }
}
