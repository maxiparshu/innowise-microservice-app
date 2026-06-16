package by.innowise.course.dto.order;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderUserDto {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String email;
}