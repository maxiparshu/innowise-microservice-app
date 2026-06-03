package by.innowise.course.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtParseFilterTest {

    @InjectMocks
    private JwtParseFilter jwtParseFilter;

    private final String secret = "super-secret-key-must-be-at-least-32-chars-long";

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(jwtParseFilter, "secret", secret);
    }

    @Test
    void validTokensSetsAttributes() throws Exception {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .claim("userId", 123L)
                .claim("role", "ADMIN")
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        jwtParseFilter.doFilterInternal(request, response, filterChain);

        assertEquals(123L, request.getAttribute("X-User-Id"));
        assertEquals("ADMIN", request.getAttribute("X-Role"));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void InvalidTokenAttributesAreNull() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        jwtParseFilter.doFilterInternal(request, response, filterChain);

        assertNull(request.getAttribute("X-User-Id"));
        verify(filterChain).doFilter(request, response);
    }
}