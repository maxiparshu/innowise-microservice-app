package by.innowise.course.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PaymentCardResponseDto {

    private Long id;

    private String number;

    private String holder;

    private LocalDate expirationDate;

    private Boolean active;

    private Long userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}