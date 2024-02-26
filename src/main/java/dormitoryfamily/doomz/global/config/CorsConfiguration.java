package dormitoryfamily.doomz.global.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://110.13.43.89/3000")
//                .allowedOrigins("http://IOS서버")
                .allowCredentials(true);
    }
}


/**
 * SOP : Same Origin Policy (동일 출처 정책)
 * CORS : Cross Origin Resource Sharing (교차 출처 자원 ..)
 *
 * 과거에는 하나의 서버에 리소드들이 다 한 곳에 있었다.
 * 동일한 도메인(출처)에서 정보를 GET 할 수 있었다.
 *
 * 다른 서버에서 리소스를 가져와야 하는 경우. (구글 MAP API)
 * 기본적으로 SOP 정책때문에 오류가...
 *
 * 소민님이 페이스북에 로그인을 한다.
 * 소민님의 크롬에는 쿠키로 페이스북의 로그인 정보를 가지고 있음
 *
 * 동민 해커가 소민님의 네이버 메일로 짝사랑하는 남자의 정보를 알고 있으니 여기에 접속하세요...라는 메일을 보냄. (www.바이러스.com)
 * 크롬이 GET. 자바스크립트... <- 소민님의 크롬 쿠키에서 페이스북 정보를 빼서, 이걸로 페이스북 로그인을 한뒤 "나는 짝사랑 중입니다"..
 *
 * 브라우저는. 다른 도메인(출처)가 다른 소스에 대해서 기본적으로 거부한다... 보안정책
 *
 * 그런데 SOP 유용한 리소스들도 다 못가져 오는 상황.
 * CORS 정책을 만들테니, 허용하는 도메인(출처)를 알려주면 내가 그것은 받아줄께.
 *
 * 크롬에서 GET 요청을 할 때 HTTP 헤더에 origin: 소민님의 url을 보낸다.
 * Access-control-allowed-origin: 소민님의 url
 * 아 여기는 안전한 곳이네... 그래서 받는다.
 *
 *
 * 1. 프론트 서버에서 프록시 서버를 만들어 전달
 * 2. origin 헤더 바꾸는 것. (귀찬고) 코드로 헤더 내용을 넣준다.
 * 3. 스프링 설정하는 것
 *
 * 프론트랑, IOs의 서버를 허용하는 것을 해줘야 한다.
 *
 * 브라우저 --->
 */
