package by.innowise.course.dto;

import by.innowise.course.utils.TestDataFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserRequestDtoTest {
    @ParameterizedTest
    @MethodSource("equalObjectsProvider")
    void shouldReturnTrueForEqualsAndSameHashCode(UserRequestDto first,
                                                  UserRequestDto second) {
        assertEquals(first.hashCode(), first.hashCode());
        assertEquals(first, second);
        assertEquals(second, first);
        assertEquals(first, first);
        assertEquals(first,second);
        assertEquals(first.hashCode(), second.hashCode());
        assertEquals(first.toString(), second.toString());
    }

    @ParameterizedTest
    @MethodSource("notEqualObjectsProvider")
    void shouldReturnFalseForEquals(UserRequestDto first,
                                    UserRequestDto second) {
        assertNotEquals(first, second);
        assertNotEquals(null, first);
    }

    private static Stream<Arguments> equalObjectsProvider() {
        UserRequestDto first = createUser();
        UserRequestDto second = createUser();

        return Stream.of(
                Arguments.of(first, second)
        );
    }

    private static Stream<Arguments> notEqualObjectsProvider() {


        UserRequestDto base = createUser();

        UserRequestDto differentEmail = createUser();
        differentEmail.setEmail(base.getEmail().repeat(2));

        UserRequestDto differentName = createUser();
        differentName.setName(base.getName().repeat(2));

        UserRequestDto differentSurname = createUser();
        differentSurname.setSurname(base.getSurname().repeat(2));

        UserRequestDto differentBirthDate = createUser();
        differentBirthDate.setBirthDate(base.getBirthDate().plusYears(200));

        return Stream.of(
                Arguments.of(base, differentName),
                Arguments.of(base, differentSurname),
                Arguments.of(base, differentEmail),
                Arguments.of(base, differentBirthDate)
        );
    }

    private static UserRequestDto createUser() {
        return TestDataFactory.createUserRequestDto();
    }
}