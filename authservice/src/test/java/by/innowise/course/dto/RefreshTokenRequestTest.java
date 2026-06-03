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

class RefreshTokenRequestTest {
    private static Validator validator;

    @Test
    void shouldBeEqualWhenSameValues() {
        RefreshTokenRequest r1 = new RefreshTokenRequest();
        r1.setRefreshToken("token");

        RefreshTokenRequest r2 = new RefreshTokenRequest();
        r2.setRefreshToken("token");

        assertEquals(r1.hashCode(), r1.hashCode());
        assertEquals(r1, r2);
        assertEquals(r2, r1);
        assertEquals(r1, r1);
        assertEquals(r1,r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertEquals(r1.toString(), r2.toString());

    }

    @Test
    void shouldNotBeEqualWhenDifferentValues() {
        RefreshTokenRequest r1 = new RefreshTokenRequest();
        r1.setRefreshToken("token1");

        RefreshTokenRequest r2 = new RefreshTokenRequest();
        r2.setRefreshToken("token2");

        assertNotEquals(r1, r2);
    }

    @Test
    void shouldHaveToStringContainingField() {
        RefreshTokenRequest r = new RefreshTokenRequest();
        r.setRefreshToken("token");

        String result = r.toString();

        assertTrue(result.contains("token"));
        assertTrue(result.contains("refreshToken"));
    }

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailWhenRefreshTokenBlank() {

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("");

        Set<ConstraintViolation<RefreshTokenRequest>> violations =
                validator.validate(request);

        assertFalse(violations.isEmpty());

        request = new RefreshTokenRequest();
        request.setRefreshToken(null);

        violations = validator.validate(request);

        assertFalse(violations.isEmpty());
    }
}