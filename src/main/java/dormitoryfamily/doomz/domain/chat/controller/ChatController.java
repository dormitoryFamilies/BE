package dormitoryfamily.doomz.domain.chat.controller;

import dormitoryfamily.doomz.domain.chat.dto.response.ChatListResponseDto;
import dormitoryfamily.doomz.domain.chatRoom.service.ChatRoomService;
import dormitoryfamily.doomz.domain.chat.service.ChatService;
import dormitoryfamily.doomz.domain.chat.dto.ChatDto;
import dormitoryfamily.doomz.global.redis.RedisPublisher;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @MessageMapping("/message")
    public void message(ChatDto chatDto) {
        chatRoomService.enterChatRoom(chatDto.getRoomUUID());
        redisPublisher.publish(chatRoomService.getTopic(chatDto.getRoomUUID()), chatDto);
        chatService.saveChat(chatDto);
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ResponseDto<ChatListResponseDto>> findAllChatHistory(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long roomId
    ){
        ChatListResponseDto responseDto = chatService.findAllChatHistory(principalDetails, roomId);
        return ResponseEntity.ok(ResponseDto.okWithData(responseDto));
    }
}
