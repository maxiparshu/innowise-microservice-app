package by.innowise.course.dto;

import by.innowise.course.utils.TestDataFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class UserResponseDtoTest {

    @ParameterizedTest
    @MethodSource("equalObjectsProvider")
    void shouldReturnTrueForEqualsAndSameHashCode(UserResponseDto first,
                                                  UserResponseDto second) {
        assertEquals(first.hashCode(), first.hashCode());
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertEquals(first.toString(), second.toString());
    }

    @ParameterizedTest
    @MethodSource("notEqualObjectsProvider")
    void shouldReturnFalseForEquals(UserResponseDto first,
                                    UserResponseDto second) {
        assertNotEquals(first, second);
        assertNotEquals(null, first);
    }

    private static Stream<Arguments> equalObjectsProvider() {
        LocalDateTime now = LocalDateTime.now();
        UserResponseDto first = createUser(now, now);
        UserResponseDto second = createUser(now, now);

        return Stream.of(
                Arguments.of(first, second)
        );
    }

    private static Stream<Arguments> notEqualObjectsProvider() {

        LocalDateTime now = LocalDateTime.now();

        UserResponseDto base = createUser(now, now);

        UserResponseDto differentId = createUser(now, now);
        differentId.setId(base.getId() + 200L);

        UserResponseDto differentEmail = createUser(now, now);
        differentEmail.setEmail(base.getEmail().repeat(2));

        UserResponseDto differentName = createUser(now, now);
        differentName.setName(base.getName().repeat(2));

        UserResponseDto differentSurname = createUser(now, now);
        differentSurname.setSurname(base.getSurname().repeat(2));

        UserResponseDto differentBirthDate = createUser(now, now);
        differentBirthDate.setBirthDate(base.getBirthDate().plusYears(200));

        UserResponseDto differentStatus = createUser(now, now);
        differentStatus.setActive(!base.getActive());

        UserResponseDto differentCards = createUser(now, now);
        PaymentCardResponseDto cardResponseDto = TestDataFactory.createPaymentCardResponseDto();
        cardResponseDto.setActive(!cardResponseDto.getActive());
        differentCards.setCards(List.of(cardResponseDto));

        UserResponseDto differentCreate = createUser(now.minusDays(1), now);
        UserResponseDto differentUpdate = createUser(now, now.plusDays(1));

        return Stream.of(
                Arguments.of(base, differentId),
                Arguments.of(base, differentName),
                Arguments.of(base, differentSurname),
                Arguments.of(base, differentEmail),
                Arguments.of(base, differentBirthDate),
                Arguments.of(base, differentStatus),
                Arguments.of(base, differentCards),
                Arguments.of(base, differentCreate),
                Arguments.of(base, differentUpdate)
        );
    }

    private static UserResponseDto createUser(LocalDateTime createdAt,
                                              LocalDateTime updatedAt) {
        UserResponseDto dto = TestDataFactory.createUserResponseDto();
        dto.setCreatedAt(createdAt);
        dto.setUpdatedAt(updatedAt);
        return dto;
    }
}