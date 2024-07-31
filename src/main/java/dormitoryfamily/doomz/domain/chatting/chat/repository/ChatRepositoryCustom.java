package dormitoryfamily.doomz.domain.chatting.chat.repository;

import dormitoryfamily.doomz.domain.chatting.chat.entity.Chat;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface ChatRepositoryCustom {
    Slice<Chat> findByChatMessage(Member member, String keyword, Pageable pageable, String sortType);
}
