package by.innowise.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateTokenResponse {
    private boolean valid;
    private Long userId;
    private String role;
}