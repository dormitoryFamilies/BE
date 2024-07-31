package dormitoryfamily.doomz.domain.roomatewish.service;

import dormitoryfamily.doomz.domain.member.dto.response.MemberInfoResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfilePagingListResponseDto;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roomatewish.entity.RoommateWish;
import dormitoryfamily.doomz.domain.roomatewish.exception.AlreadyWishedRoommateException;
import dormitoryfamily.doomz.domain.roomatewish.exception.CannotWishYourselfException;
import dormitoryfamily.doomz.domain.roomatewish.exception.RoommateWishNotExitException;
import dormitoryfamily.doomz.domain.roomatewish.repository.RoommateWishRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class RoommateWishService {

    private final RoommateWishRepository roommateWishRepository;
    private final MemberRepository memberRepository;

    public void saveRoommateWish(PrincipalDetails principalDetails, Long memberId) {
        Member loginMember = principalDetails.getMember();
        Member wishedMember = getMemberById(memberId);

        validateRoommateWishRequest(loginMember, wishedMember);

        RoommateWish roommateWish = RoommateWish.createRoommateWish(loginMember, wishedMember);
        roommateWishRepository.save(roommateWish);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
    }

    private void validateRoommateWishRequest(Member loginMember, Member wishedMember) {
        checkDistinctMembers(loginMember, wishedMember);
        checkIfAlreadyWished(loginMember, wishedMember);
    }

    private void checkDistinctMembers(Member loginMember, Member wishedMember) {
        if (Objects.equals(loginMember.getId(), wishedMember.getId())) {
            throw new CannotWishYourselfException();
        }
    }

    private void checkIfAlreadyWished(Member loginMember, Member wishedMember) {
        if (roommateWishRepository.findByWisherAndWished(loginMember, wishedMember).isPresent()) {
            throw new AlreadyWishedRoommateException();
        }
    }

    public void removeRoommateWish(PrincipalDetails principalDetails, Long memberId) {
        Member loginMember = principalDetails.getMember();
        Member wishedMember = getMemberById(memberId);
        RoommateWish roommateWish = getRoommateWishByWisherAndWished(loginMember, wishedMember);

        roommateWishRepository.delete(roommateWish);
    }

    private RoommateWish getRoommateWishByWisherAndWished(Member loginMember, Member wishedMember) {
        return roommateWishRepository.findByWisherAndWished(loginMember, wishedMember)
                .orElseThrow(RoommateWishNotExitException::new);
    }

    public MemberProfilePagingListResponseDto findMyRoommateWishes(PrincipalDetails principalDetails, Pageable pageable) {
        Member loginMember = principalDetails.getMember();

        Slice<RoommateWish> roommateWishes = roommateWishRepository.findByWisherOrderByCreatedAtDesc(loginMember, pageable);
        List<MemberInfoResponseDto> memberInfoResponseDtos = convertToMemberInfoResponseDtoList(roommateWishes.getContent());

        return MemberProfilePagingListResponseDto.from(roommateWishes, memberInfoResponseDtos);
    }

    private List<MemberInfoResponseDto> convertToMemberInfoResponseDtoList(List<RoommateWish> wishes) {
        return wishes.stream()
                .map(wish -> MemberInfoResponseDto.fromEntity(wish.getWished()))
                .collect(Collectors.toList());
    }
}