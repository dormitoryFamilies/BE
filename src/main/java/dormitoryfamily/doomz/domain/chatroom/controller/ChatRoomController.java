package dormitoryfamily.doomz.domain.chatroom.controller;

import dormitoryfamily.doomz.domain.chatroom.dto.response.ChatRoomIdResponseDto;
import dormitoryfamily.doomz.domain.chatroom.dto.response.UnreadChatCountResponseDto;
import dormitoryfamily.doomz.domain.chatroom.dto.response.ChatRoomListResponseDto;
import dormitoryfamily.doomz.domain.chatroom.dto.response.ChatRoomEntryResponseDto;
import dormitoryfamily.doomz.domain.chatroom.service.ChatRoomService;
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
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/members/{memberId}")
    public ResponseEntity<ResponseDto<ChatRoomEntryResponseDto>> createChatRoom(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        ChatRoomEntryResponseDto responseDto = chatRoomService.createChatRoom(memberId, principalDetails);
        return ResponseEntity.ok(ResponseDto.createdWithData(responseDto));
    }

    @PatchMapping("/members/{memberId}")
    public ResponseEntity<ResponseDto<ChatRoomEntryResponseDto>> reEnterChatRoom(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        ChatRoomEntryResponseDto responseDto = chatRoomService.reEnterChatRoom(memberId, principalDetails);
        return ResponseEntity.ok(ResponseDto.createdWithData(responseDto));
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<ResponseDto<ChatRoomIdResponseDto>> findChatRoomByMember(
            @PathVariable Long memberId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        ChatRoomIdResponseDto responseDto = chatRoomService.findChatRoomByMember(memberId, principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<ResponseDto<Void>> deleteRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        chatRoomService.deleteChatRoom(roomId, principalDetails);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @DeleteMapping("/rooms/{roomId}/no-messages")
    public ResponseEntity<ResponseDto<Void>> deleteEmptyRoom(
            @PathVariable Long roomId
    ) {
        chatRoomService.deleteEmptyChatRoom(roomId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/rooms")
    public ResponseEntity<ResponseDto<ChatRoomListResponseDto>> findAllChatRooms(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ) {
        ChatRoomListResponseDto responseDto = chatRoomService.findAllChatRooms(principalDetails, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @PatchMapping("/rooms/{roomId}")
    public ResponseEntity<ResponseDto<Void>> exitChatRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        chatRoomService.exitChatRoom(principalDetails, roomId);
        return ResponseEntity.ok(ResponseDto.ok());
    }

    @GetMapping("/rooms/unread")
    public ResponseEntity<ResponseDto<UnreadChatCountResponseDto>> countTotalUnreadChat(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        UnreadChatCountResponseDto responseDto = chatRoomService.countTotalUnreadChat(principalDetails);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }

    @GetMapping("/rooms/search")
    public ResponseEntity<ResponseDto<ChatRoomListResponseDto>> searchChatRooms(
            @ModelAttribute @Valid SearchRequestDto requestDto,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Pageable pageable
    ) {
        ChatRoomListResponseDto responseDto = chatRoomService.searchChatRooms(principalDetails, requestDto, pageable);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
