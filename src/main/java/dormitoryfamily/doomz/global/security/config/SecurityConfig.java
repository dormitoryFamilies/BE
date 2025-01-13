package dormitoryfamily.doomz.global.security.config;

import dormitoryfamily.doomz.global.jwt.JWTAuthenticationFilter;
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
import java.util.Collections;

import static dormitoryfamily.doomz.global.jwt.JWTProperties.HEADER_STRING_ACCESS;
import static dormitoryfamily.doomz.global.jwt.JWTProperties.HEADER_STRING_REFRESH;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
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
                    configuration.addAllowedOriginPattern("http://localhost:3001");
                    configuration.addAllowedOriginPattern("http://localhost:3000");
                    configuration.addAllowedOriginPattern("http://localhost:5500/");
                    configuration.addAllowedOriginPattern("http://34.64.76.111/");
                    configuration.addAllowedOriginPattern("http://13.124.186.20:8080/");
                    configuration.setAllowedMethods(
                            Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD"));

                    // 다른 도메인도 필요에 따라 추가
                    configuration.setAllowCredentials(true); // 쿠키를 포함한 크로스 도메인 요청을 허용
                    configuration.addExposedHeader("Accesstoken");  // 프론트에서 헤더 볼 수 있도록 허용
                    configuration.addExposedHeader("Refreshtoken");  // 프론트에서 헤더 볼 수 있도록 허용
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
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
        http.addFilterAfter(jwtAuthenticationFilter, OAuth2LoginAuthenticationFilter.class);

        // 경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/reissue", "/api/logout", "/api/login", "api/members/me/authorities").permitAll()
                .requestMatchers("/", "/stomp/**").permitAll()
                .requestMatchers("/api/my/authorities").permitAll() //개발용으로 설정함. 삭제 예정
                .requestMatchers("/api/images").hasAnyRole("VISITOR", "REJECTED_MEMBER")
                .requestMatchers("/api/members/initial-profiles", "/api/members/check").hasAnyRole("VISITOR", "REJECTED_MEMBER")
                .requestMatchers("/api/verify/**").hasRole("ADMIN")
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().hasRole("VERIFIED_STUDENT"));

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
        roleHierarchy.setHierarchy("""
                ROLE_ADMIN > ROLE_VERIFIED_STUDENT
                ROLE_VERIFIED_STUDENT > ROLE_MEMBER
                ROLE_MEMBER > ROLE_VISITOR
                ROLE_MEMBER > ROLE_REJECTED_MEMBER
                """);
        return roleHierarchy;
    }

}
