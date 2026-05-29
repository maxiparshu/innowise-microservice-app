package by.innowise.course.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }

    @Test
    void shouldBeEqualWhenFieldsSame() {

        LoginRequest r1 = new LoginRequest();
        r1.setLogin("john");
        r1.setPassword("Password1");

        LoginRequest r2 = new LoginRequest();
        r2.setLogin("john");
        r2.setPassword("Password1");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertEquals(r1.toString(), r2.toString());
    }

    @Test
    void shouldNotBeEqualWhenFieldsDifferent() {

        LoginRequest base = new LoginRequest();
        base.setLogin("john");
        base.setPassword("Password1");

        LoginRequest r1 = new LoginRequest();
        r1.setLogin("mike");
        r1.setPassword("Password1");

        LoginRequest r2 = new LoginRequest();
        r2.setLogin("john");
        r2.setPassword("Password2");

        assertNotEquals(base, r1);
        assertNotEquals(base, r2);
    }

    @Test
    void shouldFailWhenLoginBlank() {

        LoginRequest request = new LoginRequest();
        request.setLogin("");
        request.setPassword("Password1");

        Set<ConstraintViolation<LoginRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenPasswordBlank() {

        LoginRequest request = new LoginRequest();
        request.setLogin("john");
        request.setPassword("");

        Set<ConstraintViolation<LoginRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldFailWhenPasswordInvalid() {

        LoginRequest request = new LoginRequest();
        request.setLogin("john");
        request.setPassword("123");

        Set<ConstraintViolation<LoginRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldPassValidationWhenRequestValid() {

        LoginRequest request = new LoginRequest();
        request.setLogin("john");
        request.setPassword("Password1");

        Set<ConstraintViolation<LoginRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }
}