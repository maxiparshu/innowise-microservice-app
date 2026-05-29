package by.innowise.course.service;

import by.innowise.course.dto.LoginRequest;
import by.innowise.course.dto.RefreshTokenRequest;
import by.innowise.course.dto.RegisterRequest;
import by.innowise.course.dto.TokenResponse;
import by.innowise.course.dto.ValidateTokenResponse;

public interface AuthService {

    void register(RegisterRequest request);

    TokenResponse login(LoginRequest request);

    TokenResponse refresh(RefreshTokenRequest request);

    ValidateTokenResponse validate(String token);
    void logout(RefreshTokenRequest refreshTokenRequest);

}
