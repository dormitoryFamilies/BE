package dormitoryfamily.doomz.domain.roommate.lifestyle.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch._types.Result;
import dormitoryfamily.doomz.domain.member.member.entity.Member;
import dormitoryfamily.doomz.domain.member.member.exception.MemberNotExistsException;
import dormitoryfamily.doomz.domain.member.member.repository.MemberRepository;
import dormitoryfamily.doomz.domain.roommate.lifestyle.dto.request.CreateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roommate.lifestyle.dto.request.UpdateMyLifestyleRequestDto;
import dormitoryfamily.doomz.domain.roommate.lifestyle.dto.response.LifestyleResponseDto;
import dormitoryfamily.doomz.domain.roommate.lifestyle.entity.Lifestyle;
import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.AlreadyRegisterMyLifestyleException;
import dormitoryfamily.doomz.domain.roommate.lifestyle.exception.LifestyleNotExistsException;
import dormitoryfamily.doomz.domain.roommate.lifestyle.repository.LifestyleRepository;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class LifestyleService {

    private final LifestyleRepository lifestyleRepository;
    private final MemberRepository memberRepository;
    private final ElasticsearchClient elasticsearchClient;
    private static final String LIFESTYLE_INDEX = "lifestyle_vectors";

    public void saveMyLifestyle(CreateMyLifestyleRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        checkAlreadySetLifestyle(loginMember);
        Lifestyle lifestyle = CreateMyLifestyleRequestDto.toEntity(loginMember, requestDto);
        lifestyleRepository.save(lifestyle);
        indexLifestyleVector(loginMember, lifestyle);
    }

    public void updateMyLifestyle(UpdateMyLifestyleRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        Lifestyle lifestyle = getLifestyleByMember(loginMember);
        lifestyle.updateMyLifestyle(requestDto);
        indexLifestyleVector(loginMember, lifestyle);
    }

    private void indexLifestyleVector(Member member, Lifestyle lifestyle) {
        try {
            float[] vector = convertToVector(lifestyle);

            Map<String, Object> document = new HashMap<>();
            document.put("member_id", member.getId());
            document.put("lifestyle_vector", vector);
            document.put("dormitory", member.getDormitoryType().name());

            IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                    .index(LIFESTYLE_INDEX)
                    .id(member.getId().toString())
                    .document(document)
            );

            IndexResponse response = elasticsearchClient.index(request);

            if (response.result() != Result.Created && response.result() != Result.Updated) {
                System.err.println(" 저장은 됐지만 예외적인 상태: " + response.result());
            }

        } catch (IOException e) {
            System.err.println(" Elasticsearch 저장 실패: " + e.getMessage());
            throw new RuntimeException("엘라스틱서치 저장 중 오류 발생", e);
        }
    }

    private float[] convertToVector(Lifestyle lifestyle) {
        float[] vector = new float[11];
        vector[0] = lifestyle.getSleepTimeType().getIndex();
        vector[1] = lifestyle.getWakeUpTimeType().getIndex();
        vector[2] = lifestyle.getSleepingHabitType().getIndex();
        vector[3] = lifestyle.getSleepingSensitivityType().getIndex();
        vector[4] = lifestyle.getSmokingType().getIndex();
        vector[5] = lifestyle.getDrinkingFrequencyType().getIndex();
        vector[6] = lifestyle.getCleaningFrequencyType().getIndex();
        vector[7] = lifestyle.getHeatToleranceType().getIndex();
        vector[8] = lifestyle.getColdToleranceType().getIndex();
        vector[9] = lifestyle.getPerfumeUsageType().getIndex();
        vector[10] = lifestyle.getExamPreparationType().getIndex();
        return vector;
    }

    private void checkAlreadySetLifestyle(Member loginMember) {
        if (lifestyleRepository.existsByMemberId(loginMember.getId())) {
            throw new AlreadyRegisterMyLifestyleException();
        }
    }

    private Lifestyle getLifestyleByMember(Member loginMember) {
        return lifestyleRepository.findByMemberId(loginMember.getId())
                .orElseThrow(LifestyleNotExistsException::new);
    }

    @Transactional(readOnly = true)
    public LifestyleResponseDto findLifestyle(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotExistsException::new);
        Lifestyle lifestyle = getLifestyleByMember(member);
        return LifestyleResponseDto.fromEntity(lifestyle);
    }

    /**
     * 개발용 API
     * 삭제 예정
     */
    @Transactional
    public void deleteMyLifestyle(PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        lifestyleRepository.deleteByMember(loginMember);
    }
}
