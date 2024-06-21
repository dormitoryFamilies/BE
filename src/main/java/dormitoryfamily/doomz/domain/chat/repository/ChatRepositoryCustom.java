package dormitoryfamily.doomz.domain.chat.repository;

import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


public interface ChatRepositoryCustom {
    Slice<Chat> findByChatMessage(Member member, String keyword, Pageable pageable, String sortType);
}
