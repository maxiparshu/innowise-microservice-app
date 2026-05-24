package by.innowise.course.service.impl;


import by.innowise.course.dto.PaymentCardRequestDto;
import by.innowise.course.dto.PaymentCardResponseDto;
import by.innowise.course.entity.PaymentCard;
import by.innowise.course.entity.User;
import by.innowise.course.exception.PaymentCardNotFoundException;
import by.innowise.course.exception.UserCardsLimitExceededException;
import by.innowise.course.exception.UserNotFoundException;
import by.innowise.course.mapper.PaymentCardMapper;
import by.innowise.course.repository.PaymentCardRepository;
import by.innowise.course.repository.UserRepository;
import by.innowise.course.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentCardServiceImplTest {

    @Mock
    private PaymentCardRepository paymentCardRepository;

    @Mock
    private PaymentCardMapper paymentCardMapper;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private PaymentCardServiceImpl service;

    @Test
    void shouldThrowUserNotFound() {
        Long userId = 1L;
        PaymentCardRequestDto dto = new PaymentCardRequestDto();

        when(userRepository.findByIdForUpdate(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> service.create(userId, dto)
        );

        verify(paymentCardRepository, never()).countByUserId(any());
        verify(paymentCardRepository, never()).save(any());
    }


    @Test
    void shouldThrowWhenCardLimitExceeded() {
        Long userId = 1L;
        PaymentCardRequestDto dto = new PaymentCardRequestDto();

        User user = new User();

        when(userRepository.findByIdForUpdate(userId))
                .thenReturn(Optional.of(user));

        when(paymentCardRepository.countByUserId(userId))
                .thenReturn(5L);

        assertThrows(
                UserCardsLimitExceededException.class,
                () -> service.create(userId, dto)
        );

        verify(paymentCardRepository, never()).save(any());
        verify(paymentCardMapper, never()).toEntity(any());
    }


    @Test
    void shouldCreateCardSuccessfully() {
        Long userId = 1L;
        PaymentCardRequestDto dto = TestDataFactory.createPaymentCardRequestDto();
        User user = TestDataFactory.createUser();
        PaymentCard card = TestDataFactory.createPaymentCard();
        PaymentCard saved = TestDataFactory.createPaymentCard();
        ;
        PaymentCardResponseDto response = new PaymentCardResponseDto();

        when(userRepository.findByIdForUpdate(userId))
                .thenReturn(Optional.of(user));

        when(paymentCardRepository.countByUserId(userId))
                .thenReturn(3L);

        when(paymentCardMapper.toEntity(dto))
                .thenReturn(card);

        when(paymentCardRepository.save(card))
                .thenReturn(saved);

        when(paymentCardMapper.toDto(saved))
                .thenReturn(response);

        PaymentCardResponseDto result = service.create(userId, dto);

        assertNotNull(result);

        assertEquals(result, response);
    }

    @Test
    void shouldReturnCardById() {
        Long id = 1L;

        PaymentCard card = new PaymentCard();
        PaymentCardResponseDto dto = TestDataFactory.createPaymentCardResponseDto();

        when(paymentCardRepository.findById(id))
                .thenReturn(Optional.of(card));

        when(paymentCardMapper.toDto(card))
                .thenReturn(dto);

        PaymentCardResponseDto result = service.readById(id);

        assertNotNull(result);
        assertEquals(result, dto);
    }

    @Test
    void shouldReturnAllCards() {

        Pageable pageable = PageRequest.of(0, 10);

        PaymentCard card = new PaymentCard();
        PaymentCardResponseDto dto = new PaymentCardResponseDto();

        Page<PaymentCard> page =
                new PageImpl<>(List.of(card));

        when(paymentCardRepository.findAll(pageable))
                .thenReturn(page);

        when(paymentCardMapper.toDto(card))
                .thenReturn(dto);

        Page<PaymentCardResponseDto> result =
                service.readAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(paymentCardRepository).findAll(pageable);
        verify(paymentCardMapper).toDto(card);
    }

    @Test
    void shouldReturnCardsByUserId() {

        Long userId = 1L;

        PaymentCard card = new PaymentCard();
        PaymentCardResponseDto dto = new PaymentCardResponseDto();

        Pageable pageable = PageRequest.of(0, 10);
        Page<PaymentCard> page = new PageImpl<>(List.of(card));

        when(paymentCardRepository.findByUserId(userId, pageable))
                .thenReturn(page);

        when(paymentCardMapper.toDto(card))
                .thenReturn(dto);

        Page<PaymentCardResponseDto> result =
                service.readAllByUserId(userId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(paymentCardRepository).findByUserId(userId, pageable);
        verify(paymentCardMapper).toDto(card);
    }

    @Test
    void shouldReturnActiveCards() {

        Pageable pageable = PageRequest.of(0, 10);

        PaymentCard card = new PaymentCard();
        PaymentCardResponseDto dto = new PaymentCardResponseDto();

        Page<PaymentCard> page =
                new PageImpl<>(List.of(card));

        when(paymentCardRepository.findActiveCards(pageable))
                .thenReturn(page);

        when(paymentCardMapper.toDto(card))
                .thenReturn(dto);

        Page<PaymentCardResponseDto> result =
                service.readActiveCards(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(paymentCardRepository).findActiveCards(pageable);
        verify(paymentCardMapper).toDto(card);
    }

    @Test
    void shouldReturnCardsExpiringSoon() {

        Pageable pageable = PageRequest.of(0, 10);

        PaymentCard card = new PaymentCard();
        PaymentCardResponseDto dto = new PaymentCardResponseDto();

        Page<PaymentCard> page =
                new PageImpl<>(List.of(card));

        when(paymentCardRepository.findCardsExpiringSoon(pageable))
                .thenReturn(page);

        when(paymentCardMapper.toDto(card))
                .thenReturn(dto);

        Page<PaymentCardResponseDto> result =
                service.readCardsExpiringSoon(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(paymentCardRepository).findCardsExpiringSoon(pageable);
        verify(paymentCardMapper).toDto(card);
    }

    @Test
    void shouldThrowWhenCardNotFoundInUpdate() {
        Long id = 1L;
        PaymentCardRequestDto dto = new PaymentCardRequestDto();

        when(paymentCardRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                PaymentCardNotFoundException.class,
                () -> service.update(id, dto)
        );

        verify(paymentCardRepository, never()).save(any());
        verify(paymentCardMapper, never()).toDto(any());
    }


    @Test
    void shouldUpdateCardSuccessfully() {
        Long id = 1L;
        PaymentCardRequestDto dto = TestDataFactory.createPaymentCardRequestDto();
        dto.setExpirationDate(LocalDate.now().plusYears(1));
        PaymentCard card = TestDataFactory.createPaymentCard();

        PaymentCard saved = TestDataFactory.createPaymentCard();

        PaymentCardResponseDto response = TestDataFactory.createPaymentCardResponseDto();
        response.setExpirationDate(LocalDate.now().plusYears(1));
        when(paymentCardRepository.findById(id))
                .thenReturn(Optional.of(card));

        when(paymentCardRepository.save(card))
                .thenReturn(saved);

        when(paymentCardMapper.toDto(saved))
                .thenReturn(response);

        PaymentCardResponseDto result =
                service.update(id, dto);

        assertNotNull(result);

        assertEquals(result, response);

    }

    @Test
    void shouldActivateCard() {
        Long id = 1L;


        PaymentCard card = TestDataFactory.createPaymentCard();
        PaymentCard saved = TestDataFactory.createPaymentCard();

        when(paymentCardRepository.findById(id))
                .thenReturn(Optional.of(card));

        when(paymentCardRepository.save(card))
                .thenReturn(saved);

        service.activate(id);

        verify(paymentCardRepository).save(card);
    }

    @Test
    void shouldDeactivateCard() {
        Long id = 1L;
        PaymentCard card = TestDataFactory.createPaymentCard();
        PaymentCard saved = TestDataFactory.createPaymentCard();

        when(paymentCardRepository.findById(id))
                .thenReturn(Optional.of(card));

        when(paymentCardRepository.save(card))
                .thenReturn(saved);

        service.deactivate(id);

        assertFalse(card.getActive());

        verify(paymentCardRepository).save(card);
    }

    @Test
    void shouldDeleteCard() {
        Long id = 1L;
        PaymentCard card = TestDataFactory.createPaymentCard();

        when(paymentCardRepository.findById(id))
                .thenReturn(Optional.of(card));

        service.delete(id);

        verify(paymentCardRepository).delete(card);
    }

    @Test
    void shouldThrowWhenCardNotFound() {
        Long id = 1L;

        when(paymentCardRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                PaymentCardNotFoundException.class,
                () -> service.readById(id)
        );

        when(paymentCardRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                PaymentCardNotFoundException.class,
                () -> service.activate(id)
        );

        assertThrows(
                PaymentCardNotFoundException.class,
                () -> service.deactivate(id)
        );

        assertThrows(
                PaymentCardNotFoundException.class,
                () -> service.delete(id)
        );

        verify(paymentCardMapper, never()).toDto(any());
        verify(paymentCardRepository, never()).save(any());
        verify(paymentCardRepository, never()).delete(any());
    }
}