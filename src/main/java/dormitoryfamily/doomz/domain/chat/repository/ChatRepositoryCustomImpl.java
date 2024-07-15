package dormitoryfamily.doomz.domain.chat.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dormitoryfamily.doomz.domain.chat.entity.Chat;
import dormitoryfamily.doomz.domain.chat.util.SortType;
import dormitoryfamily.doomz.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static dormitoryfamily.doomz.domain.chat.entity.QChat.chat;

@RequiredArgsConstructor
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Chat> findByChatMessage(Member member, String keyword, Pageable pageable, String sortType) {

        List<Chat> content = queryFactory.selectFrom(chat)
                .join(chat.chatRoom)
                .where(chat.message.lower().likeIgnoreCase("%" + keyword + "%")
                        .and(
                                (chat.chatRoom.initiator.eq(member).and(chat.chatRoom.initiator.isNotNull()))
                                        .or(chat.chatRoom.participant.eq(member).and(chat.chatRoom.participantEnteredAt.isNotNull()))
                        )
                )
                .orderBy(getOrderByExpression(sortType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return createSlice(content, pageable);
    }

    private OrderSpecifier<?> getOrderByExpression(String sortType) {
        return SortType.fromString(sortType).getOrderSpecifier();
    }

    private Slice<Chat> createSlice(List<Chat> content, Pageable pageable) {
        boolean hasNext = false;

        if (content.size() > pageable.getPageSize()) {
            hasNext = true;
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }
}
