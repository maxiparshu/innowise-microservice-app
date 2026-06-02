package by.innowise.course.dto;

import by.innowise.course.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull
    @Positive
    private Long userId;
    @NotBlank
    private String login;

    @ValidPassword
    private String password;
}