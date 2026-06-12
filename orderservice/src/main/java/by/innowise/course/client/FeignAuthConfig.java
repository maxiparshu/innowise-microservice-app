package by.innowise.course.client;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignAuthConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            var attrs = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();

            if (attrs == null) {
                return;
            }

            HttpServletRequest request = attrs.getRequest();

            String authHeader = request.getHeader("Authorization");

            if (authHeader != null) {
                template.header("Authorization", authHeader);
            }
        };
    }
}