package by.innowise.course.utils;

import by.innowise.course.dto.PaymentCardRequestDto;
import by.innowise.course.dto.PaymentCardResponseDto;
import by.innowise.course.dto.UserRequestDto;
import by.innowise.course.dto.UserResponseDto;
import by.innowise.course.entity.PaymentCard;
import by.innowise.course.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class TestDataFactory {

    private TestDataFactory() {
    }


    public static User createUser() {
        User user = new User();

        user.setId(1L);
        user.setName("John");
        user.setSurname("Doe");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setEmail("john.doe@mail.com");
        user.setActive(true);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return user;
    }


    public static UserRequestDto createUserRequestDto() {
        UserRequestDto dto = new UserRequestDto();

        dto.setName("John");
        dto.setSurname("Doe");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setEmail("john.doe@mail.com");

        return dto;
    }

    public static UserResponseDto createUserResponseDto() {
        UserResponseDto dto = new UserResponseDto();

        dto.setId(1L);
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setEmail("john.doe@mail.com");
        dto.setActive(true);

        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        return dto;
    }


    public static PaymentCard createPaymentCard() {
        PaymentCard card = new PaymentCard();

        card.setId(1L);
        card.setNumber("1111222233334444");
        card.setHolder("JOHN DOE");
        card.setExpirationDate(LocalDate.now().plusYears(3));
        card.setActive(true);

        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());

        card.setUser(createUser());

        return card;
    }

    public static PaymentCardRequestDto createPaymentCardRequestDto() {
        PaymentCardRequestDto dto = new PaymentCardRequestDto();

        dto.setNumber("1111222233334444");
        dto.setHolder("JOHN DOE");
        dto.setActive(true);
        dto.setExpirationDate(LocalDate.now().plusYears(3));

        return dto;
    }

    public static PaymentCardResponseDto createPaymentCardResponseDto() {
        PaymentCardResponseDto dto = new PaymentCardResponseDto();

        dto.setId(1L);
        dto.setNumber("1111222233334444");
        dto.setHolder("JOHN DOE");
        dto.setExpirationDate(LocalDate.now().plusYears(3));
        dto.setActive(true);

        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());

        return dto;
    }
}