package by.innowise.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull
    @Positive
    private Long userId;
    @NotBlank
    private String login;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
            message = """
                    Password must contain: at least 6 characters,\s
                    one uppercase letter,\s
                    one lowercase letter, and one digit\s""")
    private String password;
}