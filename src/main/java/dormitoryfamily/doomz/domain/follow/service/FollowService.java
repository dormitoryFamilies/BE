package dormitoryfamily.doomz.domain.follow.service;

import dormitoryfamily.doomz.domain.follow.entity.Follow;
import dormitoryfamily.doomz.domain.follow.exception.AlreadyFollowingException;
import dormitoryfamily.doomz.domain.follow.exception.CannotFollowYourselfException;
import dormitoryfamily.doomz.domain.follow.exception.NotFollowingMemberException;
import dormitoryfamily.doomz.domain.follow.repository.FollowRepository;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileListResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileResponseDto;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public void saveFollow(PrincipalDetails principalDetails, Long followingMemberId) {
        Member loginMember = getFollowingMemberById(principalDetails.getMember().getId());
        Member followingMember = getFollowingMemberById(followingMemberId);
        checkIfCannotFollowYourself(loginMember.getId(), followingMemberId);
        checkIfAlreadyFollowing(loginMember, followingMember);
        Follow follow = Follow.createFollow(loginMember, followingMember);
        followRepository.save(follow);
        loginMember.increaseFollowingCount();
        followingMember.increaseFollowerCount();
    }

    private Member getFollowingMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
    }

    private void checkIfCannotFollowYourself(Long loginMemberId, Long followingMemberId) {
        if (loginMemberId.equals(followingMemberId)) {
            throw new CannotFollowYourselfException();
        }
    }

    private void checkIfAlreadyFollowing(Member loginMember, Member followingMember) {
        if (followRepository.existsByFollowerAndFollowing(loginMember, followingMember)) {
            throw new AlreadyFollowingException();
        }
    }

    public void removeFollow(PrincipalDetails principalDetails, Long followingMemberId) {
        Member loginMember = getFollowingMemberById(principalDetails.getMember().getId());
        Member followingMember = getFollowingMemberById(followingMemberId);
        Follow follow = getFollowByFollowerAndFollowing(loginMember, followingMember);
        followRepository.delete(follow);
        loginMember.decreaseFollowingCount();
        followingMember.decreaseFollowerCount();
    }

    private Follow getFollowByFollowerAndFollowing(Member follower, Member following) {
        return followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(NotFollowingMemberException::new);
    }

    public MemberProfileListResponseDto getMyFollowingMemberList(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        List<Follow> followingMembers = followRepository.findAllByFollowerOrderByCreatedAtDesc(loginMember);
        List<MemberProfileResponseDto> memberProfiles = followingMembers.stream()
                .map(follow -> MemberProfileResponseDto.fromEntity(follow.getFollowing())).toList();
        return MemberProfileListResponseDto.toDto(memberProfiles);
    }
}
