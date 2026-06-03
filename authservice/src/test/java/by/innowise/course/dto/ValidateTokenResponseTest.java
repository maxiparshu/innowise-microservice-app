package by.innowise.course.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateTokenResponseTest {

    @Test
    void shouldBeEqualWhenSameValues() {
        ValidateTokenResponse r1 =
                new ValidateTokenResponse(true, 1L, "USER");

        ValidateTokenResponse r2 =
                new ValidateTokenResponse(true, 1L, "USER");

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
    void shouldNotBeEqualWhenDifferentValues() {
        ValidateTokenResponse r1 =
                new ValidateTokenResponse(true, 1L, "USER");

        ValidateTokenResponse r2 =
                new ValidateTokenResponse(false, 1L, "USER");
        ValidateTokenResponse r3 =
                new ValidateTokenResponse(true, 2L, "USER");
        ValidateTokenResponse r4 =
                new ValidateTokenResponse(true, 1L, "ADMIN");
        assertNotEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertNotEquals(r1, r4);
    }
}