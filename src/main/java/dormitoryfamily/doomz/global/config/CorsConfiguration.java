package dormitoryfamily.doomz.global.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://110.13.43.89:3000")
//                .allowedOrigins("http://IOS서버")
                .allowCredentials(true);
    }
}
