package by.innowise.course.contoller;

import by.innowise.course.dto.PaymentCardRequestDto;
import by.innowise.course.dto.PaymentCardResponseDto;
import by.innowise.course.service.PaymentCardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class PaymentCardController {

    private final PaymentCardService paymentCardService;

    @PostMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentCardResponseDto create(
            @PathVariable @Positive Long userId,
            @Valid @RequestBody PaymentCardRequestDto dto
    ) {
        return paymentCardService.create(userId, dto);
    }

    @GetMapping("/{id}")
    public PaymentCardResponseDto readById(
            @PathVariable @Positive Long id
    ) {
        return paymentCardService.readById(id);
    }

    @GetMapping
    public Page<PaymentCardResponseDto> readAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Boolean expiringSoon
    ) {
        if (Boolean.TRUE.equals(active)) {
            return paymentCardService.readActiveCards(PageRequest.of(page, size));
        }

        if (Boolean.TRUE.equals(expiringSoon)) {
            return paymentCardService.readCardsExpiringSoon(PageRequest.of(page, size));
        }

        return paymentCardService.readAll(PageRequest.of(page, size));
    }

    @GetMapping("/user/{userId}")
    public Page<PaymentCardResponseDto> readAllByUserId(
            @PathVariable @Positive Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return paymentCardService.readAllByUserId(userId, PageRequest.of(page, size));
    }

    @PutMapping("/{id}")
    public PaymentCardResponseDto update(
            @PathVariable @Positive Long id,
            @Valid @RequestBody PaymentCardRequestDto dto
    ) {
        return paymentCardService.update(id, dto);
    }

    @PatchMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(
            @PathVariable @Positive Long id
    ) {
        paymentCardService.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(
            @PathVariable @Positive Long id
    ) {
        paymentCardService.deactivate(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable @Positive Long id
    ) {
        paymentCardService.delete(id);
    }
}