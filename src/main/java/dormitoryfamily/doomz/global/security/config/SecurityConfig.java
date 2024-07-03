package dormitoryfamily.doomz.global.security.config;

import dormitoryfamily.doomz.global.jwt.JWTAuthenticationFilter;
import dormitoryfamily.doomz.global.oauth2.OAuth2LoginSuccessHandler;
import dormitoryfamily.doomz.global.oauth2.OAuth2UserService;
import dormitoryfamily.doomz.global.security.exception.handler.JWTAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2UserService oAuth2UserService;
    private final JWTAuthenticationFilter jwtAuthorizationFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final AuthenticationEntryPoint entryPoint;
    private final JWTAccessDeniedHandler deniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

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

        // HTTP Basic 인증 방식 비설정
        http.httpBasic(AbstractHttpConfigurer::disable);

        // csrf 비설정
        http.csrf(AbstractHttpConfigurer::disable);

        // Form 로그인 방식 비설정
        http.formLogin(AbstractHttpConfigurer::disable);

        // rememberme 비설정
        http.rememberMe(AbstractHttpConfigurer::disable);

        // JWT 필터 추가
        http.addFilterAfter(jwtAuthorizationFilter, OAuth2LoginAuthenticationFilter.class);

        // 경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/reissue", "/api/logout").permitAll()
                .requestMatchers("/api/members/initial-profiles", "/api/members/check").hasRole("VISITOR")
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().hasRole("VERIFIED_STUDENT"));

        //oauth2 로그인
        http.oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2UserService))
                .successHandler(oAuth2LoginSuccessHandler));

        // 세션 stateless 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling((handler) -> handler.
                authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(deniedHandler));

        return http.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_VERIFIED_STUDENT\n"
                + "ROLE_VERIFIED_STUDENT > ROLE_MEMBER\n"
                + "ROLE_MEMBER > ROLE_VISITOR");
        return roleHierarchy;
    }
}
