package dormitoryfamily.doomz.domain.chat.service;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chat.repository.ChatRepository;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatMemberType;
import dormitoryfamily.doomz.domain.chatRoom.repository.ChatRoomRepository;
import dormitoryfamily.doomz.global.redis.ChatEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static dormitoryfamily.doomz.domain.chat.entity.type.VisibleStatus.*;
import static dormitoryfamily.doomz.domain.chatRoom.entity.type.ChatMemberType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final RedisTemplate<String, ChatEntity> redisTemplateMessage;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;


    public void changeChatStatus(ChatRoom chatRoom, ChatMemberType chatMemberType) {

        List<Chat> chatsToUpdateToOnlyReceiver = new ArrayList<>();
        List<Chat> chatsToUpdateToOnlySender = new ArrayList<>();

        for (Chat chat : chatRoom.getChatList()) {
            if (chatMemberType == SENDER) {
                chatsToUpdateToOnlyReceiver.add(chat);
            } else if (chatMemberType == RECEIVER) {
                chatsToUpdateToOnlySender.add(chat);
            }
        }

        if (!chatsToUpdateToOnlyReceiver.isEmpty()) {
            chatRepository.bulkUpdateChatVisibility(chatsToUpdateToOnlyReceiver, ONLY_RECEIVER_VISIBLE);
            //redis에서도 변경
        }
        if (!chatsToUpdateToOnlySender.isEmpty()) {
            chatRepository.bulkUpdateChatVisibility(chatsToUpdateToOnlySender, ONLY_RECEIVER_VISIBLE);
            //redis에서도 변경
        }
    }
}
