package org.spring.backendspring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguere implements WebMvcConfigurer {

    // this for temp
    private static final String RESOURCE_LOCATION = "file:///C:/full/upload/";

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

     @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
        // 프론트엔드에서 'http://localhost:8088/upload/파일이름.jpg'로 요청하면
        // 서버는 이 요청을 'C:/full/upload/파일이름.jpg'에서 찾아 전송합니다.
        registry.addResourceHandler("/upload/**") // 💡 웹에서 접근할 URL 패턴
                .addResourceLocations(RESOURCE_LOCATION); // 💡 실제 파일이 저장된 로컬 경로
    }
    


    
}
