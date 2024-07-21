package dormitoryfamily.doomz.domain.member.dto.response;

import dormitoryfamily.doomz.domain.member.entity.Member;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public record NonVerifiedStudentCardsResponseDto(
        long totalElements,
        int totalPages,
        int currentPage,
        List<StudentCardResponseDto> nonVerifiedStudentCards
) {
    public static NonVerifiedStudentCardsResponseDto fromResponseDto(Page<Member> responseDtos) {
        return new NonVerifiedStudentCardsResponseDto(
                responseDtos.getTotalElements(),
                responseDtos.getTotalPages(),
                responseDtos.getNumber(),
                responseDtos.getContent().stream()
                        .map(StudentCardResponseDto::fromEntity)
                        .collect(Collectors.toList()));
    }
}
