package dormitoryfamily.doomz.domain.roomate.lifestyle.controller;

import dormitoryfamily.doomz.domain.roomate.lifestyle.dto.request.CreateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.lifestyle.dto.request.UpdateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roomate.lifestyle.dto.response.LifestyleResponseDto;
import dormitoryfamily.doomz.domain.roomate.lifestyle.service.LifestyleService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LifestyleController {

    private final LifestyleService lifestyleService;

    @PostMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<Void>> registerMyLifestyle(
            @RequestBody @Valid CreateMyLifestyleRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        lifestyleService.saveMyLifestyle(requestDto, principalDetails);
        return ResponseEntity.ok(ResponseDto.created());
    }

    @PatchMapping("/my/lifestyles")
    public ResponseEntity<ResponseDto<Void>> editMyLifestyle(
            @RequestBody @Valid UpdateMyLifestyleRequestDto requestDto,
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
