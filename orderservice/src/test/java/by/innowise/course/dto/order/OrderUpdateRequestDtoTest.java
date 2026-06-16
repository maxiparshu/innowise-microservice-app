package by.innowise.course.dto.order;

import by.innowise.course.dto.orderitem.OrderItemRequestDto;
import by.innowise.course.entity.OrderStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderUpdateRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void gettersAndSettersShouldWork() {
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setItemId(1L);
        item.setQuantity(2);

        OrderUpdateRequestDto dto = new OrderUpdateRequestDto();

        dto.setStatus(OrderStatus.CREATED);
        dto.setItems(List.of(item));

        assertEquals(OrderStatus.CREATED, dto.getStatus());
        assertEquals(1, dto.getItems().size());
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setItemId(1L);
        item.setQuantity(2);

        OrderUpdateRequestDto dto1 = new OrderUpdateRequestDto();
        dto1.setStatus(OrderStatus.CREATED);
        dto1.setItems(List.of(item));

        OrderUpdateRequestDto dto2 = new OrderUpdateRequestDto();
        dto2.setStatus(OrderStatus.CREATED);
        dto2.setItems(List.of(item));

        OrderUpdateRequestDto dto3 = new OrderUpdateRequestDto();
        dto3.setStatus(OrderStatus.DELIVERED);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());

        assertEquals(dto1, dto1);
        assertNotEquals(null, dto1);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void toStringShouldContainFields() {
        OrderUpdateRequestDto dto = new OrderUpdateRequestDto();

        dto.setStatus(OrderStatus.CREATED);

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("status=CREATED"));
    }

    @Test
    void shouldPassValidationWithValidItems() {
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setItemId(1L);
        item.setQuantity(2);

        OrderUpdateRequestDto dto = new OrderUpdateRequestDto();

        dto.setStatus(OrderStatus.CREATED);
        dto.setItems(List.of(item));

        Set<ConstraintViolation<OrderUpdateRequestDto>> violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldPassValidationWhenItemsAreNull() {
        OrderUpdateRequestDto dto = new OrderUpdateRequestDto();

        dto.setStatus(OrderStatus.CREATED);
        dto.setItems(null);

        Set<ConstraintViolation<OrderUpdateRequestDto>> violations =
                validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenStatusIsNull() {
        OrderUpdateRequestDto dto = new OrderUpdateRequestDto();

        dto.setStatus(null);

        Set<ConstraintViolation<OrderUpdateRequestDto>> violations =
                validator.validate(dto);

        assertEquals(1, violations.size());

        ConstraintViolation<OrderUpdateRequestDto> violation =
                violations.iterator().next();

        assertEquals(
                "status",
                violation.getPropertyPath().toString()
        );
    }

    @Test
    void shouldFailWhenNestedItemIsInvalid() {
        OrderItemRequestDto item = new OrderItemRequestDto();

        item.setItemId(null);
        item.setQuantity(0);

        OrderUpdateRequestDto dto = new OrderUpdateRequestDto();

        dto.setStatus(OrderStatus.CREATED);
        dto.setItems(List.of(item));

        Set<ConstraintViolation<OrderUpdateRequestDto>> violations =
                validator.validate(dto);

        assertEquals(2, violations.size());
    }
}