package by.innowise.course.dto;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TokenResponseTest {

    @Test
    void shouldBeEqualWhenFieldsSame() {

        TokenResponse r1 =
                new TokenResponse("access1", "refresh1");

        TokenResponse r2 =
                new TokenResponse("access1", "refresh1");

        assertEquals(r1, r2);
        assertEquals(r1.toString(), r2.toString());
        assertEquals(r1.hashCode(), r2.hashCode());

        assertNotEquals(null, r1);
    }

    @Test
    void shouldNotBeEqualWhenFieldsDifferent() {

        TokenResponse base =
                new TokenResponse("access1", "refresh1");

        TokenResponse r1 =
                new TokenResponse("access2", "refresh1");

        TokenResponse r2 =
                new TokenResponse("access1", "refresh2");

        assertNotEquals(base, r1);
        assertNotEquals(base, r2);
    }

}