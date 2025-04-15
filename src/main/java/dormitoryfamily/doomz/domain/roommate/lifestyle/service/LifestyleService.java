package dormitoryfamily.doomz.domain.roommate.lifestyle.service;

import static dormitoryfamily.doomz.domain.roommate.util.RoommateProperties.FIELD_LIFESTYLE_VECTOR;
import static dormitoryfamily.doomz.domain.roommate.util.RoommateProperties.LIFESTYLE_INDEX;

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
import dormitoryfamily.doomz.global.elasticsearch.ElasticScriptQueryExecutor;
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
    private final ElasticScriptQueryExecutor elasticScriptQueryExecutor;

    public void saveMyLifestyle(CreateMyLifestyleRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        checkAlreadySetLifestyle(loginMember);
        Lifestyle lifestyle = CreateMyLifestyleRequestDto.toEntity(loginMember, requestDto);
        lifestyleRepository.save(lifestyle);
        elasticScriptQueryExecutor.indexLifestyleVector(loginMember, lifestyle);
    }

    public void updateMyLifestyle(UpdateMyLifestyleRequestDto requestDto, PrincipalDetails principalDetails) {
        Member loginMember = principalDetails.getMember();
        Lifestyle lifestyle = getLifestyleByMember(loginMember);
        lifestyle.updateMyLifestyle(requestDto);
        elasticScriptQueryExecutor.indexLifestyleVector(loginMember, lifestyle);
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
