package by.innowise.course.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentCardRequestDto {

    @NotBlank
    @Size(min = 13, max = 16)
    @Pattern(regexp = "\\d+")
    private String number;

    @NotBlank
    private String holder;

    @NotNull
    @Future
    private LocalDate expirationDate;

    @NotNull
    private Boolean active;
}