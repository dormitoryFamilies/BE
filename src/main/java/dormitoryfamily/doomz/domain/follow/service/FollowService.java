package dormitoryfamily.doomz.domain.follow.service;

import dormitoryfamily.doomz.domain.follow.entity.Follow;
import dormitoryfamily.doomz.domain.follow.repository.FollowRepository;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public void saveFollow(Member loginMember, Long followingMemberId){
        Member followingMember = getMemberById(followingMemberId);
        Follow follow = Follow.createFollow(loginMember, followingMember);
        followRepository.save(follow);
    }

    private Member getMemberById(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
    }
}
