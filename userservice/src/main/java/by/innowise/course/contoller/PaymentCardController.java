package by.innowise.course.contoller;

import by.innowise.course.dto.PaymentCardRequestDto;
import by.innowise.course.dto.PaymentCardResponseDto;
import by.innowise.course.service.PaymentCardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/apiv1/cards")
@RequiredArgsConstructor
public class PaymentCardController {

    private final PaymentCardService paymentCardService;

    @PostMapping("/create/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentCardResponseDto create(
            @PathVariable
            @Positive(message = "User id must be positive")
            Long userId,

            @Valid @RequestBody
            PaymentCardRequestDto dto
    ) {

        return paymentCardService.create(userId, dto);
    }

    @GetMapping("/{id}")
    public PaymentCardResponseDto readById(
            @PathVariable
            @Positive(message = "Card id must be positive")
            Long id
    ) {

        return paymentCardService.readById(id);
    }

    @GetMapping("/all")
    public Page<PaymentCardResponseDto> readAll(
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "10")
            int size
    ) {

        return paymentCardService.readAll(
                PageRequest.of(page, size)
        );
    }

    @GetMapping("/user/{userId}")
    public List<PaymentCardResponseDto> readAllByUserId(
            @PathVariable
            @Positive(message = "User id must be positive")
            Long userId
    ) {

        return paymentCardService.readAllByUserId(userId);
    }

    @GetMapping("/read-active")
    public Page<PaymentCardResponseDto> readActiveCards(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return paymentCardService.readActiveCards(
                PageRequest.of(page, size)
        );
    }

    @GetMapping("/read-expiring-soon")
    public Page<PaymentCardResponseDto> readCardsExpiringSoon(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size
    ) {

        return paymentCardService.readCardsExpiringSoon(
                PageRequest.of(page, size)
        );
    }

    @PutMapping("/{id}/update")
    public PaymentCardResponseDto update(
            @PathVariable
            @Positive(message = "Card id must be positive")
            Long id,

            @Valid @RequestBody
            PaymentCardRequestDto dto
    ) {

        return paymentCardService.update(id, dto);
    }

    @PatchMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(
            @PathVariable
            @Positive(message = "Card id must be positive")
            Long id
    ) {

        paymentCardService.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(
            @PathVariable
            @Positive(message = "Card id must be positive")
            Long id
    ) {

        paymentCardService.deactivate(id);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable
            @Positive(message = "Card id must be positive")
            Long id
    ) {

        paymentCardService.delete(id);
    }
}