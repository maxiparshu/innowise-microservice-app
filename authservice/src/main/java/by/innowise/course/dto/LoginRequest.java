package by.innowise.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginRequest {

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
