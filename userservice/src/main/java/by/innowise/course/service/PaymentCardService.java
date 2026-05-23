package by.innowise.course.service;

import by.innowise.course.dto.PaymentCardRequestDto;
import by.innowise.course.dto.PaymentCardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentCardService {
    PaymentCardResponseDto create(
            Long userId,
            PaymentCardRequestDto dto
    );

    PaymentCardResponseDto readById(Long id);

    Page<PaymentCardResponseDto> readAll(
            Pageable pageable
    );

    Page<PaymentCardResponseDto> readAllByUserId(Long userId, Pageable pageable);

    Page<PaymentCardResponseDto> readActiveCards(
            Pageable pageable
    );

    Page<PaymentCardResponseDto> readCardsExpiringSoon(
            Pageable pageable
    );

    PaymentCardResponseDto update(
            Long id,
            PaymentCardRequestDto dto
    );

    void activate(Long id);

    void deactivate(Long id);

    void delete(Long id);
}