package by.innowise.course.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeignAuthConfigTest {

    private final FeignAuthConfig config = new FeignAuthConfig();

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void shouldDoNothingWhenRequestAttributesAreNull() {
        RequestContextHolder.resetRequestAttributes();

        RequestInterceptor interceptor =
                config.requestInterceptor();

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertTrue(template.headers().isEmpty());
    }

    @Test
    void shouldAddAuthorizationHeader() {
        HttpServletRequest request =
                mock(HttpServletRequest.class);

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token");

        ServletRequestAttributes attributes =
                new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(attributes);

        RequestInterceptor interceptor =
                config.requestInterceptor();

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        Collection<String> header =
                template.headers().get("Authorization");

        assertNotNull(header);
        assertTrue(header.contains("Bearer token"));
    }

    @Test
    void shouldNotAddHeaderWhenAuthorizationHeaderIsMissing() {
        HttpServletRequest request =
                mock(HttpServletRequest.class);

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        ServletRequestAttributes attributes =
                new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(attributes);

        RequestInterceptor interceptor =
                config.requestInterceptor();

        RequestTemplate template = new RequestTemplate();

        interceptor.apply(template);

        assertFalse(
                template.headers().containsKey("Authorization")
        );
    }
}