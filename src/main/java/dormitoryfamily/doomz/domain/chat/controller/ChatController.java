package dormitoryfamily.doomz.domain.chat.controller;

import dormitoryfamily.doomz.domain.chatRoom.service.ChatRoomService;
import dormitoryfamily.doomz.domain.chat.service.ChatService;
import dormitoryfamily.doomz.global.redis.ChatEntity;
import dormitoryfamily.doomz.global.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @MessageMapping("/message")
    public void message(ChatEntity chatEntity) {
        chatRoomService.enterChatRoom(chatEntity.roomUUID());
        redisPublisher.publish(chatRoomService.getTopic(chatEntity.roomUUID()), chatEntity);
        chatService.saveChat(chatEntity);
    }
}