package by.innowise.course.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserResponseDto implements Serializable {

    private Long id;

    private String name;

    private String surname;

    private LocalDate birthDate;

    private String email;

    private Boolean active;

    private List<PaymentCardResponseDto> cards;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}