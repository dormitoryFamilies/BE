package dormitoryfamily.doomz.domain.chat.service;

import dormitoryfamily.doomz.domain.chat.dto.response.ChatListResponseDto;
import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chat.repository.ChatRepository;
import dormitoryfamily.doomz.domain.chat.dto.ChatDto;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.chatRoom.exception.ChatRoomNotExistsException;
import dormitoryfamily.doomz.domain.chatRoom.repository.ChatRoomRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final RedisTemplate<String, ChatDto> redisTemplateMessage;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void saveChat(ChatDto chatDto) {
        ChatRoom chatRoom = getChatRoomByRoomUUID(chatDto.getRoomUUID());
        Chat chat = ChatDto.toEntity(chatDto, chatRoom);
        Chat savedChat = chatRepository.save(chat);
        chatDto.setChatIdAndSentTime(savedChat.getId(), savedChat.getCreatedAt());

        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatDto.class));
        redisTemplateMessage.opsForList().rightPush(chatDto.getRoomUUID(), chatDto);
        redisTemplateMessage.expire(chatDto.getRoomUUID(), 1, TimeUnit.MINUTES);
    }

    private ChatRoom getChatRoomByRoomUUID(String roomUUID) {
        return chatRoomRepository.findByRoomUUID(roomUUID)
                .orElseThrow(ChatRoomNotExistsException::new);
    }

    public void clearChat(Long lastChatId, String roomUUID) {
        chatRepository.deleteChatsLessThanChatId(lastChatId);
        redisTemplateMessage.delete(roomUUID);  //다시 db에서 불러오기 위해 전체 삭제
    }

    public ChatListResponseDto findAllChatHistory(PrincipalDetails principalDetails, Long roomId) {
        Member loginMember = principalDetails.getMember();
        ChatRoom chatRoom = getChatRoomById(roomId);
        String roomUUID = chatRoom.getRoomUUID();

        boolean isSender = chatRoom.getSender().getId().equals(loginMember.getId());
        updateChaMemberStatusAndUnreadCount(chatRoom, isSender);

        List<ChatDto> chatList = redisTemplateMessage.opsForList().range(roomUUID, 0, -1);

        if (chatList.isEmpty()) {
            chatList = getChatListFromDatabase(roomUUID);
        }

        filterChatListByUser(chatList, chatRoom, isSender);

        return ChatListResponseDto.toDto(chatList);
    }

    private ChatRoom getChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatRoomNotExistsException::new);
    }

    private void updateChaMemberStatusAndUnreadCount(ChatRoom chatRoom, boolean isSender) {
        if (isSender) {
            chatRoom.setSenderStatusIn();
            chatRoom.resetSenderUnreadCount();
        } else {
            chatRoom.setReceiverStatusIn();
            chatRoom.resetReceiverUnreadCount();
        }
    }

    private List<ChatDto> getChatListFromDatabase(String roomUUID) {
        List<Chat> dbChatList = chatRepository.findAllByChatRoomRoomUUID(roomUUID);
        List<ChatDto> chatList = new ArrayList<>();

        for (Chat chat : dbChatList) {
            ChatDto chatDto = ChatDto.fromEntity(chat);
            chatList.add(chatDto);
            redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatDto.class));
            redisTemplateMessage.opsForList().rightPush(roomUUID, chatDto);
        }
        return chatList;
    }

    private void filterChatListByUser(List<ChatDto> chatList, ChatRoom chatRoom, boolean isSender) {
        Long lastChatId = isSender ? chatRoom.getLastReceiverOnlyChatId() : chatRoom.getLastSenderOnlyChatId();

        if (lastChatId != null) {
            chatList.removeIf(chatDto -> chatDto.getChatId() <= lastChatId);
        }
    }
}
