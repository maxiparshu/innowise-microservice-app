package by.innowise.course.service.impl;

import by.innowise.course.dto.LoginRequest;
import by.innowise.course.dto.RefreshTokenRequest;
import by.innowise.course.dto.RegisterRequest;
import by.innowise.course.dto.TokenResponse;
import by.innowise.course.dto.ValidateTokenResponse;
import by.innowise.course.entity.RefreshToken;
import by.innowise.course.entity.Role;
import by.innowise.course.entity.UserCredential;
import by.innowise.course.exception.CredentialAlreadyExistException;
import by.innowise.course.exception.RefreshTokenException;
import by.innowise.course.repository.RefreshTokenRepository;
import by.innowise.course.repository.UserCredentialRepository;
import by.innowise.course.security.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserCredentialRepository userCredentialRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserCredential user;

    @BeforeEach
    void setUp() {
        user = new UserCredential();
        user.setUserId(1L);
        user.setLogin("john");
        user.setPasswordHash("encodedPassword");
        user.setRole(Role.USER);
        user.setActive(true);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setUserId(1L);
        request.setLogin("john");
        request.setPassword("password");

        when(userCredentialRepository.findByLogin("john"))
                .thenReturn(Optional.empty());

        when(userCredentialRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("password"))
                .thenReturn("encodedPassword");

        authService.register(request);

        verify(userCredentialRepository).save(any(UserCredential.class));
    }

    @Test
    void shouldThrowWhenLoginAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setLogin("john");

        when(userCredentialRepository.findByLogin("john"))
                .thenReturn(Optional.of(user));

        assertThrows(CredentialAlreadyExistException.class,
                () -> authService.register(request));
    }

    @Test
    void shouldThrowWhenUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setLogin("john");
        request.setUserId(1L);

        when(userCredentialRepository.findByLogin("john"))
                .thenReturn(Optional.empty());

        when(userCredentialRepository.findByUserId(1L))
                .thenReturn(Optional.of(user));

        assertThrows(CredentialAlreadyExistException.class,
                () -> authService.register(request));
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest();
        request.setLogin("john");
        request.setPassword("password");

        when(userCredentialRepository.findByLogin("john"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password", "encodedPassword"))
                .thenReturn(true);

        when(jwtService.generateAccessToken(user))
                .thenReturn("accessToken");

        TokenResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());

        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void shouldThrowWhenLoginFails() {
        LoginRequest request = new LoginRequest();
        request.setLogin("john");
        request.setPassword("wrongPassword");

        when(userCredentialRepository.findByLogin("john"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> authService.login(request));
    }

    @Test
    void shouldRefreshTokenSuccessfully() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refreshToken");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refreshToken");
        refreshToken.setUserId(1L);
        refreshToken.setRevoked(false);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(1));

        when(refreshTokenRepository.findByToken("refreshToken"))
                .thenReturn(Optional.of(refreshToken));

        when(userCredentialRepository.findByUserId(1L))
                .thenReturn(Optional.of(user));

        when(jwtService.generateAccessToken(user))
                .thenReturn("newAccessToken");

        TokenResponse response = authService.refresh(request);

        assertNotNull(response);
        assertEquals("newAccessToken", response.getAccessToken());
    }

    @Test
    void shouldThrowWhenRefreshTokenRevoked() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refreshToken");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRevoked(true);

        when(refreshTokenRepository.findByToken("refreshToken"))
                .thenReturn(Optional.of(refreshToken));

        assertThrows(RefreshTokenException.class,
                () -> authService.refresh(request));
    }

    @Test
    void shouldThrowWhenRefreshTokenExpired() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refreshToken");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRevoked(false);
        refreshToken.setExpiresAt(LocalDateTime.now().minusDays(1));

        when(refreshTokenRepository.findByToken("refreshToken"))
                .thenReturn(Optional.of(refreshToken));

        assertThrows(RefreshTokenException.class,
                () -> authService.refresh(request));
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        Claims claims = mock(Claims.class);
        String token = "token";
        String tokenWithHeader = "Bearer " + token;
        when(jwtService.isValid(token))
                .thenReturn(true);

        when(jwtService.extractClaims(token))
                .thenReturn(claims);

        when(claims.get("userId", Long.class))
                .thenReturn(1L);

        when(claims.get("role", String.class))
                .thenReturn("USER");

        ValidateTokenResponse response = authService.validate(tokenWithHeader);

        assertTrue(response.isValid());
        assertEquals(1L, response.getUserId());
        assertEquals("USER", response.getRole());
    }

    @Test
    void shouldReturnInvalidValidationResponse() {
        when(jwtService.isValid("invalid"))
                .thenReturn(false);

        ValidateTokenResponse response = authService.validate("Bearer invalid");

        assertFalse(response.isValid());
        assertNull(response.getUserId());
        assertNull(response.getRole());
    }
    @Test
    void shouldFailWithWrongHeader() {
        ValidateTokenResponse response = authService.validate("invalid");

        assertFalse(response.isValid());
        assertNull(response.getUserId());
        assertNull(response.getRole());
    }

    @Test
    void shouldLogoutSuccessfully() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refreshToken");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRevoked(false);

        when(refreshTokenRepository.findByToken("refreshToken"))
                .thenReturn(Optional.of(refreshToken));

        authService.logout(request);

        assertTrue(refreshToken.getRevoked());

        verify(refreshTokenRepository).save(refreshToken);
    }

    @Test
    void shouldThrowWhenLogoutTokenNotFound() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refreshToken");

        when(refreshTokenRepository.findByToken("refreshToken"))
                .thenReturn(Optional.empty());

        assertThrows(RefreshTokenException.class,
                () -> authService.logout(request));
    }
}