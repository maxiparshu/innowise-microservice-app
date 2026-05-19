package by.innowise.course.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentCardRequestDto {

    @NotBlank
    private String number;

    @NotBlank
    private String holder;

    @NotNull
    @Future
    private LocalDate expirationDate;

    @NotNull
    private Boolean active;
}