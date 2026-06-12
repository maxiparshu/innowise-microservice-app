package by.innowise.course.dto.orderitem;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderItemRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void gettersAndSettersShouldWork() {
        OrderItemRequestDto dto = new OrderItemRequestDto();

        dto.setItemId(1L);
        dto.setQuantity(5);

        assertEquals(1L, dto.getItemId());
        assertEquals(5, dto.getQuantity());
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        OrderItemRequestDto dto1 = new OrderItemRequestDto();
        dto1.setItemId(1L);
        dto1.setQuantity(5);

        OrderItemRequestDto dto2 = new OrderItemRequestDto();
        dto2.setItemId(1L);
        dto2.setQuantity(5);

        OrderItemRequestDto dto3 = new OrderItemRequestDto();
        dto3.setItemId(2L);
        dto3.setQuantity(10);

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
        OrderItemRequestDto dto = new OrderItemRequestDto();

        dto.setItemId(1L);
        dto.setQuantity(5);

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("itemId=1"));
        assertTrue(result.contains("quantity=5"));
    }

    @Test
    void shouldPassValidation() {
        OrderItemRequestDto dto = new OrderItemRequestDto();

        dto.setItemId(1L);
        dto.setQuantity(2);

        Set<ConstraintViolation<OrderItemRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenItemIdIsNull() {
        OrderItemRequestDto dto = new OrderItemRequestDto();

        dto.setItemId(null);
        dto.setQuantity(2);

        Set<ConstraintViolation<OrderItemRequestDto>> violations =
                validator.validate(dto);

        assertEquals(1, violations.size());

        ConstraintViolation<OrderItemRequestDto> violation = violations.iterator().next();

        assertEquals("itemId", violation.getPropertyPath().toString());
    }

    @Test
    void shouldFailWhenQuantityIsNull() {
        OrderItemRequestDto dto = new OrderItemRequestDto();

        dto.setItemId(1L);
        dto.setQuantity(null);

        Set<ConstraintViolation<OrderItemRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());

        ConstraintViolation<OrderItemRequestDto> violation = violations.iterator().next();

        assertEquals("quantity", violation.getPropertyPath().toString());
    }

    @Test
    void shouldFailWhenQuantityIsZero() {
        OrderItemRequestDto dto = new OrderItemRequestDto();

        dto.setItemId(1L);
        dto.setQuantity(0);

        Set<ConstraintViolation<OrderItemRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());

        ConstraintViolation<OrderItemRequestDto> violation = violations.iterator().next();

        assertEquals("quantity", violation.getPropertyPath().toString());
    }

    @Test
    void shouldFailWhenQuantityIsNegative() {
        OrderItemRequestDto dto = new OrderItemRequestDto();

        dto.setItemId(1L);
        dto.setQuantity(-1);

        Set<ConstraintViolation<OrderItemRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());

        ConstraintViolation<OrderItemRequestDto> violation = violations.iterator().next();

        assertEquals("quantity", violation.getPropertyPath().toString());
    }
}