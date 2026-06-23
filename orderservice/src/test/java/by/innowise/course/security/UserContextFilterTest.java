package by.innowise.course.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserContextFilterTest {

    private final UserContextFilter filter = new UserContextFilter();
    private final FilterChain chain = mock(FilterChain.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSetAuthenticationWhenAttributesPresent() throws Exception {

        when(request.getHeader("X-User-Id")).thenReturn("1");
        when(request.getHeader("X-User-Role")).thenReturn("ADMIN");

        filter.doFilterInternal(request, response, chain);

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(auth);
        assertEquals(1L, auth.getPrincipal());
        assertTrue(
                auth.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals("ADMIN"))
        );

        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotSetAuthenticationWhenUserIdMissing() throws Exception {

        when(request.getHeader("X-User-Id")).thenReturn(null);
        when(request.getHeader("X-User-Role")).thenReturn("ADMIN");

        filter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldNotSetAuthenticationWhenRoleMissing() throws Exception {

        when(request.getHeader("X-User-Id")).thenReturn("1");
        when(request.getHeader("X-User-Role")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldNotSetAuthenticationWhenBothMissing() throws Exception {

        when(request.getHeader("X-User-Id")).thenReturn(null);
        when(request.getHeader("X-User-Role")).thenReturn(null);

        filter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(chain).doFilter(request, response);
    }
    @Test
    void shouldCorrectlyMapRoleToAuthority() throws Exception {

        when(request.getHeader("X-User-Id")).thenReturn("99");
        when(request.getHeader("X-User-Role")).thenReturn("USER");

        filter.doFilterInternal(request, response, chain);

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(auth);
        assertEquals(99L, auth.getPrincipal());
        assertEquals(
                List.of("USER"),
                auth.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }
}