package by.innowise.course.service;

import by.innowise.course.entity.Role;
import by.innowise.course.entity.UserCredential;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                "SuperSecretKeyForJwtTokenGeneration123456789"
        );

        ReflectionTestUtils.setField(
                jwtService,
                "expiration",
                3600000L
        );
    }

    @Test
    void shouldGenerateValidAccessToken() {
        UserCredential user = mock(UserCredential.class);

        when(user.getLogin()).thenReturn("john");
        when(user.getUserId()).thenReturn(1L);
        when(user.getRole()).thenReturn(Role.ADMIN);

        String token = jwtService.generateAccessToken(user);

        assertNotNull(token);
        assertTrue(jwtService.isValid(token));
    }

    @Test
    void shouldExtractClaimsCorrectly() {
        UserCredential user = mock(UserCredential.class);

        when(user.getLogin()).thenReturn("john");
        when(user.getUserId()).thenReturn(1L);
        when(user.getRole()).thenReturn(Role.ADMIN);

        String token = jwtService.generateAccessToken(user);

        Claims claims = jwtService.extractClaims(token);

        assertEquals("john", claims.getSubject());
        assertEquals(1, claims.get("userId", Integer.class));
        assertEquals("ADMIN", claims.get("role", String.class));
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.token.value";

        assertFalse(jwtService.isValid(invalidToken));
    }

    @Test
    void shouldReturnFalseForExpiredToken() {
        JwtService shortExpirationService = new JwtService();

        ReflectionTestUtils.setField(
                shortExpirationService,
                "secret",
                "mySuperSecretKeyForJwtTokenGeneration123456"
        );

        ReflectionTestUtils.setField(
                shortExpirationService,
                "expiration",
                0L
        );

        UserCredential user = mock(UserCredential.class);

        when(user.getLogin()).thenReturn("john");
        when(user.getUserId()).thenReturn(1L);
        when(user.getRole()).thenReturn(Role.ADMIN);

        String token = shortExpirationService.generateAccessToken(user);

        assertFalse(shortExpirationService.isValid(token));
    }
}