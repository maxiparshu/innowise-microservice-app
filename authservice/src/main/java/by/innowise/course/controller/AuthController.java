package by.innowise.course.controller;

import by.innowise.course.dto.LoginRequest;
import by.innowise.course.dto.RefreshTokenRequest;
import by.innowise.course.dto.RegisterRequest;
import by.innowise.course.dto.TokenResponse;
import by.innowise.course.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validate(
            @RequestHeader("Authorization") String token
    ) {

        return ResponseEntity.ok(authService.validate(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody RefreshTokenRequest request
    ) {

        authService.logout(request);
        return ResponseEntity.noContent().build();
    }

}

