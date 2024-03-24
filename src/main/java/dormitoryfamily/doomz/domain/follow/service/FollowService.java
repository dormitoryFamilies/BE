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

import javax.security.auth.spi.LoginModule;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public void saveFollow(PrincipalDetails principalDetails, Long followingMemberId) {
        Member loginMember = getMemberById(principalDetails.getMember().getId());
        Member followingMember = getMemberById(followingMemberId);
        validateFollowRequest(loginMember, followingMember);
        Follow follow = Follow.createFollow(loginMember, followingMember);
        saveFollowAndIncreaseCounts(loginMember, followingMember, follow);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
    }

    private void validateFollowRequest(Member loginMember, Member followingMember) {
        if (loginMember.getId().equals(followingMember.getId())) {
            throw new CannotFollowYourselfException();
        }
        if (followRepository.existsByFollowerAndFollowing(loginMember, followingMember)) {
            throw new AlreadyFollowingException();
        }
    }

    private void saveFollowAndIncreaseCounts(Member loginMember, Member followingMember, Follow follow) {
        followRepository.save(follow);
        loginMember.increaseFollowingCount();
        followingMember.increaseFollowerCount();
    }

    public void removeFollow(PrincipalDetails principalDetails, Long followingMemberId) {
        Member loginMember = getMemberById(principalDetails.getMember().getId());
        Member followingMember = getMemberById(followingMemberId);
        Follow follow = getFollowByFollowerAndFollowing(loginMember, followingMember);
        deleteFollowAndDecreaseCounts(loginMember, followingMember, follow);
    }

    private Follow getFollowByFollowerAndFollowing(Member follower, Member following) {
        return followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(NotFollowingMemberException::new);
    }

    private void deleteFollowAndDecreaseCounts(Member loginMember, Member followingMember, Follow follow) {
        followRepository.delete(follow);
        loginMember.decreaseFollowingCount();
        followingMember.decreaseFollowerCount();
    }

    public MemberProfileListResponseDto getMyFollowingMemberList(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        List<Follow> followings = followRepository.findAllByFollowerOrderByCreatedAtDesc(loginMember);
        List<MemberProfileResponseDto> memberProfiles = followings.stream()
                .map(follow -> MemberProfileResponseDto.fromEntity(follow.getFollowing())).toList();
        return MemberProfileListResponseDto.toDto(memberProfiles);
    }

    public MemberProfileListResponseDto getMyFollowerMemberList(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        List<Follow> followers = followRepository.findAllByFollowingOrderByCreatedAtDesc(loginMember);
        List<MemberProfileResponseDto> memberProfiles = followers.stream()
                .map(follow -> MemberProfileResponseDto.fromEntity(follow.getFollower())).toList();
        return MemberProfileListResponseDto.toDto(memberProfiles);
    }
}
