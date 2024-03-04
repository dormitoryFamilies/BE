package dormitoryfamily.doomz.global.security.config;

import dormitoryfamily.doomz.global.jwt.JWTAuthenticationFilter;
import dormitoryfamily.doomz.global.jwt.JWTAuthorizationFilter;
import dormitoryfamily.doomz.global.oauth2.OAuth2LoginSuccessHandler;
import dormitoryfamily.doomz.global.security.service.PrincipalOAuth2UserService;
import dormitoryfamily.doomz.global.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PrincipalOAuth2UserService principalOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정 추가
        http.cors(cors -> cors
                .configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.applyPermitDefaultValues();
                    configuration.addAllowedOriginPattern("");
                    configuration.addAllowedOriginPattern("http://localhost:3000");
                    configuration.addAllowedOriginPattern("http://43.202.254.127:8080/");
                    configuration.setAllowedMethods(
                            Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD"));

                    // 다른 도메인도 필요에 따라 추가
                    configuration.setAllowCredentials(true); // 쿠키를 포함한 크로스 도메인 요청을 허용
                    return configuration;
                }));

        // csrf 비설정
        http.csrf(AbstractHttpConfigurer::disable);

        // Form 로그인 방식 비설정
        http.formLogin(AbstractHttpConfigurer::disable);

        // HTTP Basic 인증 방식 비설정
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 세션 stateless 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //oauth2 로그인
        http.oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig ->
                        userInfoEndpointConfig.userService(principalOAuth2UserService))
                .successHandler(oAuth2LoginSuccessHandler));

        //JWT 필터 추가
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter, BasicAuthenticationFilter.class);

        // 경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .anyRequest().authenticated());

        http.exceptionHandling(exceptionHandling -> {
            exceptionHandling.authenticationEntryPoint(
                    (request, response, authException) -> CustomResponseUtil.fail(response,
                            "로그인을 진행해 주세요", HttpStatus.UNAUTHORIZED));

            exceptionHandling.accessDeniedHandler(
                    (request, response, accessDeniedException) -> CustomResponseUtil.fail(response,
                            "접근 권한이 없습니다", HttpStatus.FORBIDDEN));
        });

        return http.build();
    }

}
