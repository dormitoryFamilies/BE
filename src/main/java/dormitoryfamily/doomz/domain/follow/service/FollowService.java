package dormitoryfamily.doomz.domain.follow.service;

import dormitoryfamily.doomz.domain.follow.entity.Follow;
import dormitoryfamily.doomz.domain.follow.exception.AlreadyFollowingException;
import dormitoryfamily.doomz.domain.follow.exception.CannotFollowYourselfException;
import dormitoryfamily.doomz.domain.follow.exception.NotFollowingMemberException;
import dormitoryfamily.doomz.domain.follow.repository.FollowRepository;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileFollowResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileListResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfilePagingListResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileResponseDto;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public MemberProfilePagingListResponseDto getMyFollowingMemberList(PrincipalDetails principalDetails, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        Page<Follow> followings = followRepository.findAllByFollowerOrderByCreatedAtDesc(loginMember, pageable);
        List<MemberProfileResponseDto> memberProfiles = followings.getContent().stream()
                .map(follow -> MemberProfileResponseDto.fromEntity(follow.getFollowing()))
                .collect(Collectors.toList());
        return MemberProfilePagingListResponseDto.toDto(followings, memberProfiles);
    }

    public MemberProfileListResponseDto searchFollowings(PrincipalDetails principalDetails, SearchRequestDto requestDto) {
        Member loginMember = principalDetails.getMember();
        List<Follow> follows = followRepository.findByFollowerAndFollowingNicknameContaining(loginMember, requestDto.q());
        List<MemberProfileResponseDto> followings = follows.stream()
                .map(follow -> MemberProfileResponseDto.fromEntity(follow.getFollowing()))
                .collect(Collectors.toList());
        return MemberProfileListResponseDto.toDto(followings);
    }

    public MemberProfilePagingListResponseDto getMyFollowerMemberList(PrincipalDetails principalDetails, Pageable pageable) {
        Member loginMember = principalDetails.getMember();
        Page<Follow> followers = followRepository.findAllByFollowingOrderByCreatedAtDesc(loginMember, pageable);
        List<MemberProfileFollowResponseDto> memberProfiles = followers.getContent().stream()
                .map(follow -> {
                    Member follower = follow.getFollower();
                    boolean isFollowing = followRepository.findByFollowerAndFollowing(loginMember, follower).isPresent();
                    return MemberProfileFollowResponseDto.fromEntity(follower, isFollowing);
                })
                .collect(Collectors.toList());
        return MemberProfilePagingListResponseDto.toDto(followers, memberProfiles);
    }

    public MemberProfileListResponseDto searchFollowers(PrincipalDetails principalDetails, SearchRequestDto requestDto) {
        Member loginMember = principalDetails.getMember();
        List<Follow> follows = followRepository.findByFollowingAndFollowerNicknameContaining(loginMember, requestDto.q());
        List<MemberProfileResponseDto> followers = follows.stream()
                .map(follow -> MemberProfileResponseDto.fromEntity(follow.getFollower()))
                .collect(Collectors.toList());
        return MemberProfileListResponseDto.toDto(followers);
    }
}
