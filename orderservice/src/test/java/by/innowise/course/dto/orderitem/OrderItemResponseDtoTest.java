package by.innowise.course.dto.orderitem;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderItemResponseDtoTest {

    @Test
    void gettersAndSettersShouldWork() {
        OrderItemResponseDto dto = new OrderItemResponseDto();

        dto.setItemId(1L);
        dto.setItemName("Phone");
        dto.setItemPrice(BigDecimal.valueOf(999.99));
        dto.setQuantity(2);

        assertEquals(1L, dto.getItemId());
        assertEquals("Phone", dto.getItemName());
        assertEquals(BigDecimal.valueOf(999.99), dto.getItemPrice());
        assertEquals(2, dto.getQuantity());
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        OrderItemResponseDto dto1 = new OrderItemResponseDto();
        dto1.setItemId(1L);
        dto1.setItemName("Phone");
        dto1.setItemPrice(BigDecimal.valueOf(999.99));
        dto1.setQuantity(2);

        OrderItemResponseDto dto2 = new OrderItemResponseDto();
        dto2.setItemId(1L);
        dto2.setItemName("Phone");
        dto2.setItemPrice(BigDecimal.valueOf(999.99));
        dto2.setQuantity(2);

        OrderItemResponseDto dto3 = new OrderItemResponseDto();
        dto3.setItemId(2L);
        dto3.setItemName("Laptop");
        dto3.setItemPrice(BigDecimal.valueOf(1999.99));
        dto3.setQuantity(1);

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
        OrderItemResponseDto dto = new OrderItemResponseDto();

        dto.setItemId(1L);
        dto.setItemName("Phone");
        dto.setItemPrice(BigDecimal.valueOf(999.99));
        dto.setQuantity(2);

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("itemId=1"));
        assertTrue(result.contains("itemName=Phone"));
        assertTrue(result.contains("itemPrice=999.99"));
        assertTrue(result.contains("quantity=2"));
    }
}