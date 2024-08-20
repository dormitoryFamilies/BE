package dormitoryfamily.doomz.domain.notification.controller;

import dormitoryfamily.doomz.domain.notification.dto.request.NotificationsSetReadRequestDto;
import dormitoryfamily.doomz.domain.notification.dto.response.NotificationListResponseDto;
import dormitoryfamily.doomz.domain.notification.service.NotificationService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
    ) {
        return notificationService.subscribe(principalDetails, lastEventId);
    }

    @GetMapping("/notifications")
    public ResponseEntity<ResponseDto<NotificationListResponseDto>> findAllNotifications(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ) {
        NotificationListResponseDto responseDto = notificationService.getAllNotifications(principalDetails, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @PutMapping("/notifications/read")
    public ResponseEntity<ResponseDto<Void>> setNotificationsRead(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody NotificationsSetReadRequestDto requestDto
    ) {
        notificationService.setNotificationAsRead(principalDetails, requestDto);
        return ResponseEntity.ok(ResponseDto.ok());
    }
}
