package dormitoryfamily.doomz.domain.chat.service;

import dormitoryfamily.doomz.domain.chat.dto.response.ChatListResponseDto;
import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chat.repository.ChatRepository;
import dormitoryfamily.doomz.domain.chat.dto.ChatDto;
import dormitoryfamily.doomz.domain.chatRoom.entity.ChatRoom;
import dormitoryfamily.doomz.domain.chatRoom.exception.ChatRoomNotExistsException;
import dormitoryfamily.doomz.domain.chatRoom.repository.ChatRoomRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.global.chat.ChatMessage;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final RedisTemplate<String, ChatDto> redisTemplateMessage;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void saveChat(ChatMessage chatMessage) {
        ChatRoom chatRoom = getChatRoomByRoomUUID(chatMessage.getRoomUUID());
        Chat chat = ChatMessage.toEntity(chatMessage, chatRoom);
        ChatDto chatDto = ChatDto.fromEntity(chat);
        chatRepository.save(chat);

        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatDto.class));
        redisTemplateMessage.opsForList().rightPush(chatMessage.getRoomUUID(), chatDto);
        redisTemplateMessage.expire(chatMessage.getRoomUUID(), 1, TimeUnit.MINUTES);
    }

    private ChatRoom getChatRoomByRoomUUID(String roomUUID) {
        return chatRoomRepository.findByRoomUUID(roomUUID)
                .orElseThrow(ChatRoomNotExistsException::new);
    }

    public void clearChat(Long lastChatId, String roomUUID) {
        chatRepository.deleteChatsLessThanChatId(lastChatId);
        redisTemplateMessage.delete(roomUUID);  //다시 db에서 불러오기 위해 전체 삭제
    }

    public ChatListResponseDto findAllChatHistory(PrincipalDetails principalDetails, Long roomId, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        ChatRoom chatRoom = getChatRoomById(roomId);
        String roomUUID = chatRoom.getRoomUUID();

        boolean isSender = chatRoom.getSender().getId().equals(loginMember.getId());
        updateChaMemberStatusAndUnreadCount(chatRoom, isSender);

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();

        Long lastChatId = isSender ? chatRoom.getLastReceiverOnlyChatId() : chatRoom.getLastSenderOnlyChatId();

        ListOperations<String, ChatDto> listOps = redisTemplateMessage.opsForList();

        // Redis에 저장된 채팅 개수 확인
        Long listSize = listOps.size(roomUUID);

        // 페이징에 필요한 시작 인덱스와 종료 인덱스 계산
        int startIndex = (int) Math.max(listSize - (pageNumber + 1) * pageSize, 0);
        int endIndex = (int) Math.max(listSize - pageNumber * pageSize - 1, 0);

        List<ChatDto> chatList = listOps.range(roomUUID, startIndex, endIndex);

        if(!chatList.isEmpty() && startIndex==0 && endIndex ==0){
            return ChatListResponseDto.toDto(pageNumber, true, Collections.emptyList());
        }

        // 마지막 페이지 여부 확인
        boolean isLast = endIndex == listSize - 1;

        if (chatList.isEmpty()) {
            // Redis에 저장된 데이터가 없을 경우 DB에서 데이터 조회
            Slice<Chat> chats;
            if (lastChatId != null) {
                chats = chatRepository.findAllByChatRoomRoomUUID(roomUUID, lastChatId, pageable);
            } else {
                chats = chatRepository.findAllByChatRoomRoomUUID(roomUUID, pageable);
            }

            // DB에서 조회한 데이터를 Redis에 저장하고 chatList에 추가
            List<Chat> dbChatList = chatRepository.findAllByChatRoomRoomUUID(roomUUID);
            for (Chat chat : dbChatList) {
                ChatDto chatDto = ChatDto.fromEntity(chat);
                chatList.add(chatDto);
                redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatDto.class));
                redisTemplateMessage.opsForList().rightPush(roomUUID, chatDto);
            }

            // 마지막 페이지 여부와 현재 페이지 번호 설정
            isLast = chats.isLast();
            pageNumber = chats.getNumber();
        }

        // ChatListResponseDto 생성 및 반환
        return ChatListResponseDto.toDto(pageNumber, isLast, chatList);
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

}


