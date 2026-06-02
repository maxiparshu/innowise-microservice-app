package by.innowise.course.dto;

import by.innowise.course.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    private String login;

    @NotBlank
    @ValidPassword
    private String password;
}
