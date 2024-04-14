package dormitoryfamily.doomz.domain.chatRoom.controller;

import dormitoryfamily.doomz.domain.chatRoom.dto.response.ChatRoomListResponseDto;
import dormitoryfamily.doomz.domain.chatRoom.dto.response.CreateChatRoomResponseDto;
import dormitoryfamily.doomz.domain.chatRoom.service.ChatRoomService;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/members/{memberId}")
    public ResponseEntity<ResponseDto<CreateChatRoomResponseDto>> createRoom(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        CreateChatRoomResponseDto responseDto = chatRoomService.createChatRoom(memberId, principalDetails);
        return ResponseEntity.ok(ResponseDto.createdWithData(responseDto));
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<ResponseDto<Void>> deleteRoom(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        chatRoomService.deleteChatRoom(memberId, principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/rooms")
    public ResponseEntity<ResponseDto<ChatRoomListResponseDto>> findAllChatRooms(
            @AuthenticationPrincipal PrincipalDetails principalDetails)
    {
        ChatRoomListResponseDto responseDto = chatRoomService.findAllChatRooms(principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @PatchMapping("/rooms/{roomId}")
    public ResponseEntity<ResponseDto<Void>> exitChatRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
      chatRoomService.exitChatRoom(principalDetails, roomId);
      return ResponseEntity.ok(ResponseDto.ok());
    }
}
