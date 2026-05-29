package by.innowise.course.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class PaymentCardController {

    private final PaymentCardService paymentCardService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @accessService.isOwner(#userId)")
    public ResponseEntity<PaymentCardResponseDto> readById(
            @PathVariable @Positive Long id
    ) {
        return ResponseEntity.ok()
                .body(paymentCardService.readById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<PaymentCardResponseDto>> readAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname
    ) {
        return ResponseEntity.ok()
                .body(paymentCardService.readAll(name, surname, PageRequest.of(page, size)));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<PaymentCardResponseDto>> readAllActive(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return ResponseEntity.ok()
                .body(paymentCardService.readActiveCards(PageRequest.of(page, size)));
    }

    @GetMapping("/expiring")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<PaymentCardResponseDto>> readExpiringSoonCards(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return ResponseEntity.ok()
                .body(paymentCardService.readCardsExpiringSoon(PageRequest.of(page, size)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @accessService.isOwner(#userId)")
    public ResponseEntity<PaymentCardResponseDto> update(
            @PathVariable @Positive Long id,
            @Valid @RequestBody PaymentCardRequestDto dto
    ) {
        return ResponseEntity.ok()
                .body(paymentCardService.update(id, dto));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ADMIN') or @accessService.isOwner(#userId)")
    public ResponseEntity<Void> activate(
            @PathVariable @Positive Long id
    ) {
        paymentCardService.activate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('ADMIN') or @accessService.isOwner(#userId)")
    public ResponseEntity<Void> deactivate(
            @PathVariable @Positive Long id
    ) {
        paymentCardService.deactivate(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @accessService.isOwner(#userId)")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive Long id
    ) {
        paymentCardService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}