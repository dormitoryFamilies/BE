package dormitoryfamily.doomz.domain.member.controller;

import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileResponseDto;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.member.service.MemberService;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("members/{memberId}")
    public ResponseEntity<ResponseDto<MemberProfileResponseDto>> getMemberProfile(
            @PathVariable Long memberId
    ){
        MemberProfileResponseDto responseDto = memberService.getMemberProfile(memberId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
