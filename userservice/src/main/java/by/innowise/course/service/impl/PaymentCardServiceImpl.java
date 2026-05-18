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
import by.innowise.course.service.PaymentCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PaymentCardServiceImpl implements PaymentCardService {
    private final PaymentCardRepository paymentCardRepository;
    private final PaymentCardMapper paymentCardMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PaymentCardResponseDto create(Long userId, PaymentCardRequestDto dto) {
        long cardsCount =
                paymentCardRepository.countByUserId(userId);

        if (cardsCount >= 5) {
            throw new UserCardsLimitExceededException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(()
                        -> new UserNotFoundException(userId));

        PaymentCard card =
                paymentCardMapper.toEntity(dto);

        card.setUser(user);

        return paymentCardMapper.toDto(
                paymentCardRepository.save(card)
        );
    }

    @Override
    public PaymentCardResponseDto readById(Long id) {
        PaymentCard card = paymentCardRepository.findById(id)
                .orElseThrow(()
                        -> new PaymentCardNotFoundException(id));

        return paymentCardMapper.toDto(card);
    }

    @Override
    public Page<PaymentCardResponseDto> readAll(Pageable pageable) {

        return paymentCardRepository.findAll(pageable)
                .map(paymentCardMapper::toDto);
    }

    @Override
    public List<PaymentCardResponseDto> readAllByUserId(Long userId) {

        return paymentCardRepository.findByUserId(userId)
                .stream()
                .map(paymentCardMapper::toDto)
                .toList();
    }

    @Override
    public Page<PaymentCardResponseDto> readActiveCards(Pageable pageable) {
        return paymentCardRepository.findActiveCards(pageable)
                .map(paymentCardMapper::toDto);
    }

    @Override
    public Page<PaymentCardResponseDto> readCardsExpiringSoon(Pageable pageable) {
        return paymentCardRepository.findCardsExpiringSoon(pageable)
                .map(paymentCardMapper::toDto);
    }


    @Override
    @Transactional
    public PaymentCardResponseDto update(
            Long id,
            PaymentCardRequestDto dto
    ) {

        PaymentCard card =
                paymentCardRepository.findById(id)
                        .orElseThrow(()
                                -> new PaymentCardNotFoundException(id));

        card.setNumber(dto.getNumber());
        card.setHolder(dto.getHolder());
        card.setExpirationDate(dto.getExpirationDate());

        return paymentCardMapper.toDto(paymentCardRepository.save(card));
    }

    @Override
    @Transactional
    public void activate(Long id) {
        PaymentCard paymentCard = paymentCardRepository.findById(id)
                .orElseThrow(()
                        -> new UserNotFoundException(id)
                );

        paymentCard.setActive(true);
        paymentCardRepository.save(paymentCard);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        PaymentCard paymentCard = paymentCardRepository.findById(id)
                .orElseThrow(()
                        -> new UserNotFoundException(id)
                );

        paymentCard.setActive(false);
        paymentCardRepository.save(paymentCard);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        PaymentCard card = paymentCardRepository.findById(id)
                .orElseThrow(()
                        -> new PaymentCardNotFoundException(id));

        paymentCardRepository.delete(card);
    }
}
