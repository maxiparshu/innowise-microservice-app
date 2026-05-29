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
import by.innowise.course.service.AuthService;
import by.innowise.course.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
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
        return generateTokens(user);
    }

    @Override
    public ValidateTokenResponse validate(String token) {
        boolean valid = jwtService.isValid(token);
        if (!valid) {
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
