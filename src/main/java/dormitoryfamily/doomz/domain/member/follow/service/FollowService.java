package dormitoryfamily.doomz.domain.member.follow.service;

import dormitoryfamily.doomz.domain.member.follow.exception.AlreadyFollowingException;
import dormitoryfamily.doomz.domain.member.follow.exception.CannotFollowYourselfException;
import dormitoryfamily.doomz.domain.member.follow.exception.NotFollowingMemberException;
import dormitoryfamily.doomz.domain.member.follow.repository.FollowRepository;
import dormitoryfamily.doomz.domain.member.member.dto.response.FollowerMemberResponseDto;
import dormitoryfamily.doomz.domain.member.member.dto.response.MemberInfoResponseDto;
import dormitoryfamily.doomz.domain.member.member.dto.response.MemberProfileListResponseDto;
import dormitoryfamily.doomz.domain.member.member.dto.response.MemberProfilePagingListResponseDto;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.member.follow.entity.Follow;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import dormitoryfamily.doomz.global.util.SearchRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    public void removeFollowing(PrincipalDetails principalDetails, Long followingMemberId) {
        Member loginMember = getMemberById(principalDetails.getMember().getId());
        Member followingMember = getMemberById(followingMemberId);
        Follow follow = getFollowByFollowerAndFollowing(loginMember, followingMember);

        deleteFollowingAndDecreaseCounts(loginMember, followingMember, follow);
    }

    private Follow getFollowByFollowerAndFollowing(Member follower, Member following) {
        return followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(NotFollowingMemberException::new);
    }

    private void deleteFollowingAndDecreaseCounts(Member loginMember, Member followingMember, Follow follow) {
        followRepository.delete(follow);
        loginMember.decreaseFollowingCount();
        followingMember.decreaseFollowerCount();
    }

    public void removeFollower(PrincipalDetails principalDetails, Long memberId) {
        Member loginMember = getMemberById(principalDetails.getMember().getId());
        Member followerMember = getMemberById(memberId);
        Follow follow = getFollowByFollowerAndFollowing(followerMember, loginMember);

        deleteFollowerAndDecreaseCounts(loginMember, followerMember, follow);
    }

    private void deleteFollowerAndDecreaseCounts(Member loginMember, Member followingMember, Follow follow) {
        followRepository.delete(follow);
        loginMember.decreaseFollowerCount();
        followingMember.decreaseFollowingCount();
    }

    public MemberProfilePagingListResponseDto findFollowings(PrincipalDetails principalDetails, Pageable pageable) {
        Member loginMember = principalDetails.getMember();

        Page<Follow> follows = followRepository.findAllByFollowerOrderByCreatedAtDesc(loginMember, pageable);
        List<MemberInfoResponseDto> memberInfoDtos = convertToMemberInfoResponseDtoList(follows.getContent());

        return MemberProfilePagingListResponseDto.from(follows, memberInfoDtos);
    }

    private List<MemberInfoResponseDto> convertToMemberInfoResponseDtoList(List<Follow> follows){
        return follows.stream()
                .map(follow -> MemberInfoResponseDto.fromEntity(follow.getFollowing()))
                .collect(Collectors.toList());
    }

    public MemberProfilePagingListResponseDto searchFollowings(PrincipalDetails principalDetails, SearchRequestDto requestDto, Pageable pageable) {
        Member loginMember = principalDetails.getMember();

        Slice<Follow> follows = followRepository.findByFollowerAndFollowingNicknameContaining(loginMember, requestDto.q(), pageable);
        List<MemberInfoResponseDto> memberInfoDtos = convertToMemberInfoResponseDtoList(follows.getContent());

        return MemberProfilePagingListResponseDto.from(follows, memberInfoDtos);
    }

    public MemberProfilePagingListResponseDto findFollowers(PrincipalDetails principalDetails, Pageable pageable) {
        Member loginMember = principalDetails.getMember();

        Page<Follow> followsPage = followRepository.findAllByFollowingOrderByCreatedAtDesc(loginMember, pageable);
        List<FollowerMemberResponseDto> followers = convertToFollowerMemberResponseDtoList(loginMember, followsPage.getContent());

        return MemberProfilePagingListResponseDto.from(followsPage, followers);
    }

    private List<FollowerMemberResponseDto> convertToFollowerMemberResponseDtoList(Member loginMember, List<Follow> follows) {
        return follows.stream()
                .map(follow -> createFollowerMemberResponseDto(loginMember, follow.getFollower()))
                .collect(Collectors.toList());
    }

    private FollowerMemberResponseDto createFollowerMemberResponseDto(Member loginMember, Member follower) {
        boolean isFollowing = followRepository.existsByFollowerAndFollowing(loginMember, follower);
        return FollowerMemberResponseDto.fromEntity(follower, isFollowing);
    }

    public MemberProfilePagingListResponseDto searchFollowers(PrincipalDetails principalDetails, SearchRequestDto requestDto, Pageable pageable) {
        Member loginMember = principalDetails.getMember();

        Slice<Follow> follows = followRepository.findByFollowingAndFollowerNicknameContaining(loginMember, requestDto.q(), pageable);
        List<FollowerMemberResponseDto> memberProfiles = convertToFollowerMemberResponseDtoList(loginMember, follows.getContent());

        return MemberProfilePagingListResponseDto.from(follows, memberProfiles);
    }

}
