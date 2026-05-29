package by.innowise.course.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class ErrorResponseDtoTest {

    @ParameterizedTest
    @MethodSource("equalObjectsProvider")
    void shouldReturnTrueForEqualsAndSameHashCode(ErrorResponseDto first,
                                                  ErrorResponseDto second) {
        assertEquals(first.hashCode(), first.hashCode());
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
        assertEquals(first.toString(), second.toString());
    }

    @ParameterizedTest
    @MethodSource("notEqualObjectsProvider")
    void shouldReturnFalseForEquals(ErrorResponseDto first,
                                    ErrorResponseDto second) {

        assertNotEquals(first, second);
        assertNotEquals(null, first);
    }

    private static Stream<Arguments> equalObjectsProvider() {

        LocalDateTime now = LocalDateTime.now();

        ErrorResponseDto first = createError(
                now,
                404,
                "NOT_FOUND",
                "Resource not found"
        );

        ErrorResponseDto second = createError(
                now,
                404,
                "NOT_FOUND",
                "Resource not found"
        );

        return Stream.of(
                Arguments.of(first, second)
        );
    }

    private static Stream<Arguments> notEqualObjectsProvider() {

        LocalDateTime now = LocalDateTime.now();

        ErrorResponseDto base = createError(
                now,
                404,
                "NOT_FOUND",
                "Resource not found"
        );

        ErrorResponseDto differentError = createError(
                now,
                404,
                "INTERNAL_SERVER_ERROR",
                "Resource not found"
        );
        ErrorResponseDto differentStatus = createError(
                now,
                501,
                "INTERNAL_SERVER_ERROR",
                "Resource not found"
        );
        ErrorResponseDto differentMessage = createError(
                now,
                404,
                "NOT_FOUND",
                "Another message"
        );
        ErrorResponseDto differentTime = createError(
                now.plusHours(10),
                404,
                "NOT_FOUND",
                "Another message"
        );

        return Stream.of(
                Arguments.of(base, differentStatus),
                Arguments.of(base, differentMessage),
                Arguments.of(base, differentTime),
                Arguments.of(base, differentError)
        );
    }

    private static ErrorResponseDto createError(LocalDateTime timestamp,
                                                int status,
                                                String error,
                                                String message) {

        return ErrorResponseDto.builder()
                .timestamp(timestamp)
                .status(status)
                .error(error)
                .message(message)
                .build();
    }
}