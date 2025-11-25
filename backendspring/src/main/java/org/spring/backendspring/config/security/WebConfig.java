package org.spring.backendspring.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 🔥 http://localhost:8088/uploadImg/파일명 으로 접근 가능하게 하는 설정
        registry.addResourceHandler("/upload/**")
<<<<<<< HEAD
=======
                .addResourceLocations("file:///E:/uploadImg/");
                
        registry.addResourceHandler("/uploadImg/**")
                .addResourceLocations("file:///C:/full/upload/");
>>>>>>> 3e7b7b00091813cb069bf27a8228798e546da19f

                .addResourceLocations("file:///E:/full/upload/",
                        "file:///C:/full/upload/",
                        "file:///D:/full/upload/");
         
        registry.addResourceHandler("/uploadImg/**")
                .addResourceLocations("file:///C:/full/upload/");                
                
        // registry.addResourceHandler("/upload/**")
        //         .addResourceLocations("file:///C:/full/upload/");

        // registry.addResourceHandler("/upload/**")
        //         .addResourceLocations("file:///D:/full/upload/");

    }
}
