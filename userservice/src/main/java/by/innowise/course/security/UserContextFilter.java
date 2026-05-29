package by.innowise.course.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class UserContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Object userIdAttr =
                request.getAttribute("X-User-Id");

        Object roleAttr =
                request.getAttribute("X-Role");

        if (userIdAttr != null && roleAttr != null) {

            Long userId = (Long) userIdAttr;

            String role = (String) roleAttr;

            var authorities = List.of(
                    new SimpleGrantedAuthority(role)
            );

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}