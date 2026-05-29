package by.innowise.course.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterRequestTest {

    private static Validator validator;

    static Stream<Arguments> invalidRequests() {
        return Stream.of(
                Arguments.of(
                        build(null, "john", "Password1")
                ),
                Arguments.of(
                        build(-1L, "john", "Password1")
                ),
                Arguments.of(
                        build(1L, "", "Password1")
                ),
                Arguments.of(
                        build(1L, "john", "")
                ),
                Arguments.of(
                        build(1L, "john", "123")
                )
        );
    }

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }

    @Test
    void shouldBeEqualWhenAllFieldsSame() {

        RegisterRequest r1 = new RegisterRequest();
        r1.setUserId(1L);
        r1.setLogin("john");
        r1.setPassword("Password1");

        RegisterRequest r2 = new RegisterRequest();
        r2.setUserId(1L);
        r2.setLogin("john");
        r2.setPassword("Password1");

        assertEquals(r1.hashCode(), r1.hashCode());
        assertEquals(r1, r2);
        assertEquals(r2, r1);
        assertEquals(r1, r1);
        assertEquals(r1,r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertEquals(r1.toString(), r2.toString());

        assertNotEquals(null, r1);


    }

    @Test
    void shouldNotBeEqualWhenUserDifferent() {

        RegisterRequest r1 = new RegisterRequest();
        r1.setUserId(1L);
        r1.setLogin("john");
        r1.setPassword("Password1");

        RegisterRequest r2 = new RegisterRequest();
        r2.setUserId(2L);
        r2.setLogin("john");
        r2.setPassword("Password1");

        assertNotEquals(r1, r2);

        RegisterRequest r3 = new RegisterRequest();
        r2.setUserId(1L);
        r2.setLogin("mike");
        r2.setPassword("Password1");

        assertNotEquals(r1, r3);

        RegisterRequest r4 = new RegisterRequest();
        r2.setUserId(1L);
        r2.setLogin("john");
        r2.setPassword("Password2");

        assertNotEquals(r1, r4);
    }


    @Test
    void shouldPassValidationWhenRequestValid() {

        RegisterRequest request = new RegisterRequest();
        request.setUserId(1L);
        request.setLogin("john");
        request.setPassword("Password1");

        Set<ConstraintViolation<RegisterRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }
    @ParameterizedTest
    @MethodSource("invalidRequests")
    void shouldFailValidation(RegisterRequest request) {

        Set<ConstraintViolation<RegisterRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    private static RegisterRequest build(Long userId, String login, String password) {
        RegisterRequest r = new RegisterRequest();
        r.setUserId(userId);
        r.setLogin(login);
        r.setPassword(password);
        return r;
    }
}