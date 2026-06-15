package by.innowise.course.dto.item;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemResponseDtoTest {

    @Test
    void gettersAndSettersShouldWork() {
        ItemResponseDto dto = new ItemResponseDto();

        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 2, 12, 0);

        dto.setId(1L);
        dto.setName("Phone");
        dto.setPrice(BigDecimal.valueOf(999.99));
        dto.setCreatedAt(createdAt);
        dto.setUpdatedAt(updatedAt);

        assertEquals(1L, dto.getId());
        assertEquals("Phone", dto.getName());
        assertEquals(BigDecimal.valueOf(999.99), dto.getPrice());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 1, 2, 12, 0);

        ItemResponseDto dto1 = new ItemResponseDto();
        dto1.setId(1L);
        dto1.setName("Phone");
        dto1.setPrice(BigDecimal.valueOf(999.99));
        dto1.setCreatedAt(createdAt);
        dto1.setUpdatedAt(updatedAt);

        ItemResponseDto dto2 = new ItemResponseDto();
        dto2.setId(1L);
        dto2.setName("Phone");
        dto2.setPrice(BigDecimal.valueOf(999.99));
        dto2.setCreatedAt(createdAt);
        dto2.setUpdatedAt(updatedAt);

        ItemResponseDto dto3 = new ItemResponseDto();
        dto3.setId(2L);
        dto3.setName("Laptop");
        dto3.setPrice(BigDecimal.valueOf(1999.99));

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
        ItemResponseDto dto = new ItemResponseDto();

        dto.setId(1L);
        dto.setName("Phone");
        dto.setPrice(BigDecimal.valueOf(999.99));

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name=Phone"));
        assertTrue(result.contains("price=999.99"));
    }
}