package by.innowise.course.dto.order;

import by.innowise.course.dto.orderitem.OrderItemResponseDto;
import by.innowise.course.entity.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderResponseDtoTest {

    @Test
    void gettersAndSettersShouldWork() {
        UserResponseDto user = new UserResponseDto();
        user.setName("John");

        OrderItemResponseDto item = new OrderItemResponseDto();
        item.setItemId(1L);

        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 10, 0);

        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 2, 10, 0);

        OrderResponseDto dto = new OrderResponseDto();

        dto.setId(1L);
        dto.setUserId(10L);
        dto.setStatus(OrderStatus.CREATED);
        dto.setTotalPrice(BigDecimal.valueOf(100));
        dto.setUser(user);
        dto.setItems(List.of(item));
        dto.setCreatedAt(createdAt);
        dto.setUpdatedAt(updatedAt);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getUserId());
        assertEquals(OrderStatus.CREATED, dto.getStatus());
        assertEquals(BigDecimal.valueOf(100), dto.getTotalPrice());
        assertEquals(user, dto.getUser());
        assertEquals(1, dto.getItems().size());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        UserResponseDto user = new UserResponseDto();
        user.setName("John");
        user.setSurname("Doe");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setEmail("john@test.com");

        OrderResponseDto dto1 = new OrderResponseDto();
        dto1.setId(1L);
        dto1.setUserId(10L);
        dto1.setStatus(OrderStatus.CREATED);
        dto1.setTotalPrice(BigDecimal.valueOf(100));
        dto1.setUser(user);

        OrderResponseDto dto2 = new OrderResponseDto();
        dto2.setId(1L);
        dto2.setUserId(10L);
        dto2.setStatus(OrderStatus.CREATED);
        dto2.setTotalPrice(BigDecimal.valueOf(100));
        dto2.setUser(user);

        OrderResponseDto dto3 = new OrderResponseDto();
        dto3.setId(2L);
        dto3.setUserId(20L);
        dto3.setStatus(OrderStatus.DELIVERED);
        dto3.setTotalPrice(BigDecimal.valueOf(200));

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
        OrderResponseDto dto = new OrderResponseDto();

        dto.setId(1L);
        dto.setUserId(10L);
        dto.setStatus(OrderStatus.CREATED);
        dto.setTotalPrice(BigDecimal.valueOf(100));

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("userId=10"));
        assertTrue(result.contains("status=CREATED"));
        assertTrue(result.contains("totalPrice=100"));
    }
}