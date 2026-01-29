package dormitoryfamily.doomz.domain.roommate.lifestyle.controller;

import dormitoryfamily.doomz.domain.roommate.lifestyle.dto.request.LifestyleRequestDto;
import dormitoryfamily.doomz.domain.roommate.lifestyle.dto.request.LifestyleRequestDto.CreateValidation;
import dormitoryfamily.doomz.domain.roommate.lifestyle.dto.request.LifestyleRequestDto.UpdateValidation;
import dormitoryfamily.doomz.domain.roommate.lifestyle.dto.response.LifestyleResponseDto;
import dormitoryfamily.doomz.domain.roommate.lifestyle.service.LifestyleService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LifestyleController {

    private final LifestyleService lifestyleService;

    @PostMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<Void>> registerMyLifestyle(
            @RequestBody @Validated(CreateValidation.class) LifestyleRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        lifestyleService.saveMyLifestyle(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @PatchMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<Void>> editMyLifestyle(
            @RequestBody @Validated(UpdateValidation.class) LifestyleRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        lifestyleService.updateMyLifestyle(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<LifestyleResponseDto>> getMyLifestyle(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        LifestyleResponseDto responseDto =
                lifestyleService.findLifestyle(principalDetails.getMember().getId());
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/members/{memberId}/lifestyles")
    public ResponseEntity<ResponseDto<LifestyleResponseDto>> getLifestyle(
            @PathVariable Long memberId
    ) {
        LifestyleResponseDto responseDto =
                lifestyleService.findLifestyle(memberId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    /**
     * 개발용 삭제 API
     * 실제 서비스시 삭제 예정
     */
    @DeleteMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<Void>> deleteMyLifestyle(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        lifestyleService.deleteMyLifestyle(principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }
}
