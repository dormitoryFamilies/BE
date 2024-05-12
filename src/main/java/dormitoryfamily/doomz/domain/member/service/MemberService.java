package dormitoryfamily.doomz.domain.member.service;

import dormitoryfamily.doomz.domain.member.dto.request.MyProfileModifyRequestDto;
import dormitoryfamily.doomz.domain.member.dto.response.MemberProfileResponseDto;
import dormitoryfamily.doomz.domain.member.dto.response.MyProfileResponseDto;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.entity.type.CollegeType;
import dormitoryfamily.doomz.domain.member.entity.type.DepartmentType;
import dormitoryfamily.doomz.domain.member.entity.type.MemberDormitoryType;
import dormitoryfamily.doomz.domain.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberProfileResponseDto getMemberProfile(Long memberId){
        Member member = getMemberById(memberId);
        return  MemberProfileResponseDto.fromEntity(member);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistsException::new);
    }

    public MyProfileResponseDto getMyProfile(PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        return MyProfileResponseDto.fromEntity(member);
    }

    public void modifyMyProfile(MyProfileModifyRequestDto requestDto, PrincipalDetails principalDetails) {
        Member member = getMemberById(principalDetails.getMember().getId());
        updateProfileFields(requestDto, member);
    }

    private void updateProfileFields(MyProfileModifyRequestDto requestDto, Member member) {
        updateFieldIfNotNullOrEmpty(requestDto.nickname(), member::setNickname);
        updateDormitoryType(requestDto.memberDormitoryType(), member);
        updateCollegeType(requestDto.collegeType(), member);
        updateDepartmentType(requestDto.departmentType(), requestDto.collegeType(), member);
        updateFieldIfNotNullOrEmpty(requestDto.studentNumber(), member::setStudentNumber);
        updateFieldIfNotNullOrEmpty(requestDto.profileUrl(), member::setProfileUrl);
    }

    private void updateFieldIfNotNullOrEmpty(String value, Consumer<String> setter) {
        if (value != null && !value.isEmpty()) {
            setter.accept(value);
        }
    }

    private void updateDormitoryType(String dormitoryTypeDescription, Member member) {
        if (dormitoryTypeDescription != null && !dormitoryTypeDescription.isEmpty()) {
            member.setDormitoryType(MemberDormitoryType.fromDescription(dormitoryTypeDescription));
        }
    }

    private void updateCollegeType(String collegeTypeDescription, Member member) {
        if (collegeTypeDescription != null && !collegeTypeDescription.isEmpty()) {
            member.setCollegeType(CollegeType.fromDescription(collegeTypeDescription));
        }
    }

    private void updateDepartmentType(String departmentTypeDescription, String collegeTypeDescription, Member member) {
        if (departmentTypeDescription != null && !departmentTypeDescription.isEmpty()) {
            member.setDepartmentType(DepartmentType.fromDescription(departmentTypeDescription, CollegeType.fromDescription(collegeTypeDescription)));
        }
    }
}
