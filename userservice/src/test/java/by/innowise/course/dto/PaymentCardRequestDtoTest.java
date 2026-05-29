package by.innowise.course.dto;


import by.innowise.course.utils.TestDataFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PaymentCardRequestDtoTest {
    @ParameterizedTest
    @MethodSource("equalObjectsProvider")
    void shouldReturnTrueForEqualsAndSameHashCode(PaymentCardRequestDto first,
                                                  PaymentCardRequestDto second) {
        assertEquals(first.hashCode(), first.hashCode());
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertEquals(first.toString(), second.toString());
    }

    @ParameterizedTest
    @MethodSource("notEqualObjectsProvider")
    void shouldReturnFalseForEquals(PaymentCardRequestDto first,
                                    PaymentCardRequestDto second) {
        assertNotEquals(first, second);
        assertNotEquals(null, first);
    }

    private static Stream<Arguments> equalObjectsProvider() {
        PaymentCardRequestDto first = createCard();
        PaymentCardRequestDto second = createCard();
        return Stream.of(
                Arguments.of(first, second)
        );
    }

    private static Stream<Arguments> notEqualObjectsProvider() {

        LocalDate expirationDate = LocalDate.of(2030, 12, 31);

        PaymentCardRequestDto base = createCard();

        PaymentCardRequestDto differentNumber = createCard();
        differentNumber.setNumber("5555666677778888");
        PaymentCardRequestDto differentHolder = createCard();
        differentHolder.setHolder(base.getHolder().repeat(2));
        PaymentCardRequestDto differentStatus = createCard();
        differentStatus.setActive(!differentStatus.getActive());
        PaymentCardRequestDto differentExpiration = createCard();
        differentExpiration.setExpirationDate(expirationDate);
        return Stream.of(
                Arguments.of(base, differentNumber),
                Arguments.of(base, differentHolder),
                Arguments.of(base, differentExpiration),
                Arguments.of(base, differentStatus)
        );
    }

    private static PaymentCardRequestDto createCard() {
        return TestDataFactory.createPaymentCardRequestDto();
    }
}