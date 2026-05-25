package by.innowise.course.controller;

import by.innowise.course.dto.PaymentCardRequestDto;
import by.innowise.course.dto.PaymentCardResponseDto;
import by.innowise.course.dto.UserRequestDto;
import by.innowise.course.dto.UserResponseDto;
import by.innowise.course.service.PaymentCardService;
import by.innowise.course.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PaymentCardService paymentCardService;

    @PostMapping("/{userId}/payment-cards")
    public ResponseEntity<PaymentCardResponseDto> create(
            @PathVariable @Positive Long userId,
            @Valid @RequestBody PaymentCardRequestDto dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentCardService.create(userId, dto));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(
            @Valid @RequestBody UserRequestDto dto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> readById(
            @Positive(message = "Id must be positive") @PathVariable Long id
    ) {

        return ResponseEntity.ok()
                .body(userService.readById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> readAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {

        return ResponseEntity.ok()
                .body(userService.readAll(name, surname, PageRequest.of(page, size)));
    }

    @GetMapping("/active")
    public ResponseEntity<Page<UserResponseDto>> readActiveUsers(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {

        return ResponseEntity.ok().body(userService.readActiveUsers(
                PageRequest.of(page, size)
        ));
    }
    @GetMapping("/{userId}/payment-cards")
    public Page<PaymentCardResponseDto> readAllByUserId(
            @PathVariable @Positive Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return paymentCardService.readAllByUserId(userId, PageRequest.of(page, size));
    }

    @GetMapping("/without-cards")
    public ResponseEntity<Page<UserResponseDto>> readUsersWithoutCards(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {

        return ResponseEntity.ok().body(userService.readUsersWithoutCards(
                PageRequest.of(page, size)
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @Positive(message = "Id must be positive") @PathVariable Long id,
            @Valid @RequestBody UserRequestDto dto
    ) {

        return ResponseEntity.ok(userService.update(id, dto));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activate(
            @Positive(message = "Id must be positive") @PathVariable Long id
    ) {
        userService.activate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivate(
            @Positive(message = "Id must be positive") @PathVariable Long id
    ) {

        userService.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Positive(message = "Id must be positive") @PathVariable Long id
    ) {

        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}