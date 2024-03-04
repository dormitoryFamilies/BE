package dormitoryfamily.doomz.global.security.service;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.entity.type.GenderType;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.PrincipalDetails;
import dormitoryfamily.doomz.global.security.dto.KakaoMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        KakaoMemberResponse kakaoMemberResponse = new KakaoMemberResponse(oAuth2User.getAttributes());

        Optional<Member> user = memberRepository.findByEmail(kakaoMemberResponse.getEmail());

        // 첫 로그인이라면
        if (user.isEmpty()) {
            Member newUser = Member.builder()
                    .name(kakaoMemberResponse.getName())
                    .email(kakaoMemberResponse.getEmail())
                    .birthDate(kakaoMemberResponse.getBirthDate())
                    .profileUrl(kakaoMemberResponse.getProfileImage())
                    .genderType(kakaoMemberResponse.getGender())
                    .authority("ROLE_USER")
                    .build();

            memberRepository.save(newUser);
            return new PrincipalDetails(newUser);
        }

        return new PrincipalDetails(user.get());
    }
}
