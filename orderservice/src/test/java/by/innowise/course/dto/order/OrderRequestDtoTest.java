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

import static org.junit.jupiter.api.Assertions.*;

class OrderRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void gettersAndSettersShouldWork() {
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setItemId(1L);
        item.setQuantity(2);

        OrderRequestDto dto = new OrderRequestDto();

        dto.setUserId(10L);
        dto.setStatus(OrderStatus.CREATED);
        dto.setItems(List.of(item));

        assertEquals(10L, dto.getUserId());
        assertEquals(OrderStatus.CREATED, dto.getStatus());
        assertEquals(1, dto.getItems().size());
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setItemId(1L);
        item.setQuantity(2);

        OrderRequestDto dto1 = new OrderRequestDto();
        dto1.setUserId(10L);
        dto1.setStatus(OrderStatus.CREATED);
        dto1.setItems(List.of(item));

        OrderRequestDto dto2 = new OrderRequestDto();
        dto2.setUserId(10L);
        dto2.setStatus(OrderStatus.CREATED);
        dto2.setItems(List.of(item));

        OrderRequestDto dto3 = new OrderRequestDto();
        dto3.setUserId(20L);
        dto3.setStatus(OrderStatus.DELIVERED);
        dto3.setItems(List.of());

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
        OrderRequestDto dto = new OrderRequestDto();

        dto.setUserId(10L);
        dto.setStatus(OrderStatus.CREATED);
        dto.setItems(List.of());

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("userId=10"));
        assertTrue(result.contains("status=CREATED"));
    }

    @Test
    void shouldPassValidation() {
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setItemId(1L);
        item.setQuantity(2);

        OrderRequestDto dto = new OrderRequestDto();

        dto.setUserId(10L);
        dto.setStatus(OrderStatus.CREATED);
        dto.setItems(List.of(item));

        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailWhenUserIdIsNull() {
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setItemId(1L);
        item.setQuantity(2);

        OrderRequestDto dto = new OrderRequestDto();

        dto.setUserId(null);
        dto.setStatus(OrderStatus.CREATED);
        dto.setItems(List.of(item));

        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailWhenStatusIsNull() {
        OrderItemRequestDto item = new OrderItemRequestDto();
        item.setItemId(1L);
        item.setQuantity(2);

        OrderRequestDto dto = new OrderRequestDto();

        dto.setUserId(10L);
        dto.setStatus(null);
        dto.setItems(List.of(item));

        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailWhenItemsAreNull() {
        OrderRequestDto dto = new OrderRequestDto();

        dto.setUserId(10L);
        dto.setStatus(OrderStatus.CREATED);
        dto.setItems(null);

        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailWhenItemsAreEmpty() {
        OrderRequestDto dto = new OrderRequestDto();

        dto.setUserId(10L);
        dto.setStatus(OrderStatus.CREATED);
        dto.setItems(List.of());

        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
    }

    @Test
    void shouldFailWhenNestedItemIsInvalid() {
        OrderItemRequestDto item = new OrderItemRequestDto();

        item.setItemId(null);
        item.setQuantity(0);

        OrderRequestDto dto = new OrderRequestDto();

        dto.setUserId(10L);
        dto.setStatus(OrderStatus.CREATED);
        dto.setItems(List.of(item));

        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(dto);

        assertEquals(2, violations.size());
    }
}