package dormitoryfamily.doomz.global.oauth2;

import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.dto.KakaoMemberResponse;
import dormitoryfamily.doomz.global.security.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("====== OAuth2UserService ======");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        KakaoMemberResponse kakaoMemberResponse = new KakaoMemberResponse(oAuth2User.getAttributes());

        Optional<Member> user = memberRepository.findByEmail(kakaoMemberResponse.getEmail());

        //첫 로그인이라면
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
