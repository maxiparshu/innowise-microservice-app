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
import by.innowise.course.service.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        boolean loginExists = userCredentialRepository.findByLogin(request.getLogin()).isPresent();
        if (loginExists) {
            throw new CredentialAlreadyExistException();
        }
        boolean userExists = userCredentialRepository.findByUserId(request.getUserId()).isPresent();
        if (userExists) {
            throw new CredentialAlreadyExistException();
        }
        UserCredential credential = new UserCredential();
        credential.setUserId(request.getUserId());
        credential.setLogin(request.getLogin());
        credential.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        credential.setRole(Role.USER);
        credential.setActive(true);
        userCredentialRepository.save(credential);
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        UserCredential user = userCredentialRepository.findByLogin(request.getLogin()).orElseThrow(() -> new BadCredentialsException("Invalid login or password"));
        if (!user.getActive()) {
            throw new DisabledException("User account is disabled");
        }
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid login or password");
        }
        return generateTokens(user);
    }

    @Override
    public TokenResponse refresh(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (refreshToken.getRevoked()) {
            throw new RefreshTokenException("Refresh token revoked");
        }
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenException("Refresh token expired");
        }

        UserCredential user = userCredentialRepository.findByUserId(refreshToken.getUserId())
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        return generateTokens(user);
    }

    @Override
    public ValidateTokenResponse validate(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ValidateTokenResponse(false, null, null);
        }

        String token = authHeader.substring(7);

        if (!jwtService.isValid(token)) {
            return new ValidateTokenResponse(false, null, null);
        }

        Claims claims = jwtService.extractClaims(token);
        return new ValidateTokenResponse(true,
                claims.get("userId", Long.class),
                claims.get("role", String.class));
    }

    @Override
    public void logout(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    private TokenResponse generateTokens(UserCredential user) {
        refreshTokenRepository.findByUserId(user.getUserId())
                .stream()
                .filter(token -> !token.getRevoked())
                .forEach(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });

        String accessToken = jwtService.generateAccessToken(user);
        String refreshTokenValue = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(user.getUserId());
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(7));
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);
        return new TokenResponse(accessToken, refreshTokenValue);
    }
}
