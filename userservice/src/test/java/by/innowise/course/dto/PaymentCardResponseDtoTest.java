package by.innowise.course.dto;

import by.innowise.course.utils.TestDataFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class PaymentCardResponseDtoTest {

    @ParameterizedTest
    @MethodSource("equalObjectsProvider")
    void shouldReturnTrueForEqualsAndSameHashCode(PaymentCardResponseDto first,
                                                  PaymentCardResponseDto second) {
        assertEquals(first.hashCode(), first.hashCode());
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertEquals(first.toString(), second.toString());
    }

    @ParameterizedTest
    @MethodSource("notEqualObjectsProvider")
    void shouldReturnFalseForEquals(PaymentCardResponseDto first,
                                    PaymentCardResponseDto second) {
        assertNotEquals(first, second);
        assertNotEquals(null, first);
    }

    private static Stream<Arguments> equalObjectsProvider() {
        LocalDateTime now = LocalDateTime.now();
        PaymentCardResponseDto first = createCard(now, now);
        PaymentCardResponseDto second = createCard(now, now);
        return Stream.of(
                Arguments.of(first, second)
        );
    }

    private static Stream<Arguments> notEqualObjectsProvider() {

        LocalDate expirationDate = LocalDate.of(2030, 12, 31);
        LocalDateTime now = LocalDateTime.now();

        PaymentCardResponseDto base = createCard(now, now);

        PaymentCardResponseDto differentId = createCard(now, now);
        differentId.setId(2L);
        PaymentCardResponseDto differentNumber = createCard(now, now);
        differentNumber.setNumber("5555666677778888");
        PaymentCardResponseDto differentHolder = createCard(now, now);
        differentHolder.setHolder(base.getHolder().repeat(2));
        PaymentCardResponseDto differentStatus = createCard(now, now);
        differentStatus.setActive(!differentStatus.getActive());
        PaymentCardResponseDto differentExpiration = createCard(now, now);
        differentExpiration.setExpirationDate(expirationDate);
        PaymentCardResponseDto differentCreate = createCard(now.minusDays(1), now);
        PaymentCardResponseDto differentUpdate = createCard(now, now.plusDays(1));
        return Stream.of(
                Arguments.of(base, differentId),
                Arguments.of(base, differentNumber),
                Arguments.of(base, differentHolder),
                Arguments.of(base, differentExpiration),
                Arguments.of(base, differentStatus),
                Arguments.of(base, differentCreate),
                Arguments.of(base, differentUpdate)
        );
    }

    private static PaymentCardResponseDto createCard(LocalDateTime createdAt,
                                                     LocalDateTime updatedAt) {
        PaymentCardResponseDto dto = TestDataFactory.createPaymentCardResponseDto();
        dto.setCreatedAt(createdAt);
        dto.setUpdatedAt(updatedAt);
        return dto;
    }
}