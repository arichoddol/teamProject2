package org.spring.backendspring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguere implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // TODO Auto-generated method stub
      registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins("http://localhost:3000","http://localhost:3001") // ⭐ Frontend Origin 명시
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(true) // 쿠키/인증 정보 허용
                .maxAge(3600); // 캐싱 시간 설정
    }

    


    
}
