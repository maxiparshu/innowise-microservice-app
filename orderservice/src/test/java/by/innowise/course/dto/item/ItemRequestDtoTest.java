package by.innowise.course.dto.item;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemRequestDtoTest {
    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }

    @Test
    void gettersAndSettersShouldWork() {
        ItemRequestDto dto = new ItemRequestDto();

        dto.setName("Phone");
        dto.setPrice(BigDecimal.valueOf(999.99));

        assertEquals("Phone", dto.getName());
        assertEquals(BigDecimal.valueOf(999.99), dto.getPrice());
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        ItemRequestDto dto1 = new ItemRequestDto();
        dto1.setName("Phone");
        dto1.setPrice(BigDecimal.valueOf(999.99));

        ItemRequestDto dto2 = new ItemRequestDto();
        dto2.setName("Phone");
        dto2.setPrice(BigDecimal.valueOf(999.99));

        ItemRequestDto dto3 = new ItemRequestDto();
        dto3.setName("Laptop");
        dto3.setPrice(BigDecimal.valueOf(1999.99));

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());

        assertEquals(dto1, dto1);
        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void toStringShouldContainFields() {
        ItemRequestDto dto = new ItemRequestDto();

        dto.setName("Phone");
        dto.setPrice(BigDecimal.valueOf(999.99));

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("name=Phone"));
        assertTrue(result.contains("price=999.99"));
    }


    @Test
    void shouldPassValidation() {
        ItemRequestDto dto = new ItemRequestDto();

        dto.setName("Phone");
        dto.setPrice(BigDecimal.valueOf(100));

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenNameIsNull() {
        ItemRequestDto dto = new ItemRequestDto();

        dto.setName(null);
        dto.setPrice(BigDecimal.valueOf(100));

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());

        ConstraintViolation<ItemRequestDto> violation = violations.iterator().next();

        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        ItemRequestDto dto = new ItemRequestDto();

        dto.setName("   ");
        dto.setPrice(BigDecimal.valueOf(100));

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());

        ConstraintViolation<ItemRequestDto> violation = violations.iterator().next();

        assertEquals("name", violation.getPropertyPath().toString());
    }

    @Test
    void shouldFailWhenPriceIsNull() {
        ItemRequestDto dto = new ItemRequestDto();

        dto.setName("Phone");
        dto.setPrice(null);

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());

        ConstraintViolation<ItemRequestDto> violation = violations.iterator().next();

        assertEquals("price", violation.getPropertyPath().toString());
    }

    @Test
    void shouldFailWhenPriceIsZero() {
        ItemRequestDto dto = new ItemRequestDto();

        dto.setName("Phone");
        dto.setPrice(BigDecimal.ZERO);

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());

        ConstraintViolation<ItemRequestDto> violation = violations.iterator().next();

        assertEquals("price", violation.getPropertyPath().toString());
    }

    @Test
    void shouldFailWhenPriceIsNegative() {
        ItemRequestDto dto = new ItemRequestDto();

        dto.setName("Phone");
        dto.setPrice(BigDecimal.valueOf(-10));

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());

        ConstraintViolation<ItemRequestDto> violation = violations.iterator().next();

        assertEquals("price", violation.getPropertyPath().toString());
    }
}