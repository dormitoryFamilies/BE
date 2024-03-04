package dormitoryfamily.doomz.global.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import dormitoryfamily.doomz.domain.member.dto.request.LoginRequestDto;
import dormitoryfamily.doomz.domain.member.dto.response.LoginResponseDto;
import dormitoryfamily.doomz.domain.member.entity.Member;
import dormitoryfamily.doomz.domain.member.repository.MemberRepository;
import dormitoryfamily.doomz.global.security.PrincipalDetails;
import dormitoryfamily.doomz.global.util.ResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;

    /**
     * 로그인 인증 시도
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // 요청된 JSON 데이터를 객체로 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            LoginRequestDto loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            // 로그인할 때 입력한 email과 password를 가지고 authenticationToken를 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.email(),
                    loginRequest.password(),
                    new ArrayList<>(List.of(new SimpleGrantedAuthority("ROLE_USER")))
            );

            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException {
        Member member = ((PrincipalDetails) authResult.getPrincipal()).getMember();
        String token = jwtUtil.createToken(member);

        LoginResponseDto loginResponseDto = LoginResponseDto.fromEntity(member, token);
        ResponseDto<LoginResponseDto> loginResponse = ResponseDto.okWithData(loginResponseDto);

        sendJsonResponse(response, loginResponse, HttpStatus.OK);
    }

    /**
     * 인증실패
     */
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        String authenticationErrorMessage = getAuthenticationErrorMessage(exception);

        ResponseDto<Void> errorResponse = ResponseDto.errorWithMessage(HttpStatus.BAD_REQUEST,
                authenticationErrorMessage);
        sendJsonResponse(response, errorResponse, HttpStatus.BAD_REQUEST);
    }

    private void sendJsonResponse(HttpServletResponse response, Object responseData,
                                  HttpStatus httpStatus) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String jsonResponse = objectMapper.writeValueAsString(responseData);

        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }

    private String getAuthenticationErrorMessage(AuthenticationException exception) {
        if (exception instanceof BadCredentialsException) {
            return "이메일 또는 비밀번호 에러";
        } else if (exception instanceof UsernameNotFoundException) {
            return "존재하지 않는 유저";
        } else {
            return "인증 실패";
        }
    }
}
