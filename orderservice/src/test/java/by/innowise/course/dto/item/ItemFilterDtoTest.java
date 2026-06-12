package by.innowise.course.dto.item;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemFilterDtoTest {

    @Test
    void gettersAndSettersShouldWork() {
        ItemFilterDto dto = new ItemFilterDto();

        dto.setName("Phone");
        dto.setMinPrice(BigDecimal.valueOf(100));
        dto.setMaxPrice(BigDecimal.valueOf(1000));

        assertEquals("Phone", dto.getName());
        assertEquals(BigDecimal.valueOf(100), dto.getMinPrice());
        assertEquals(BigDecimal.valueOf(1000), dto.getMaxPrice());
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        ItemFilterDto dto1 = new ItemFilterDto();
        dto1.setName("Phone");
        dto1.setMinPrice(BigDecimal.valueOf(100));
        dto1.setMaxPrice(BigDecimal.valueOf(1000));

        ItemFilterDto dto2 = new ItemFilterDto();
        dto2.setName("Phone");
        dto2.setMinPrice(BigDecimal.valueOf(100));
        dto2.setMaxPrice(BigDecimal.valueOf(1000));

        ItemFilterDto dto3 = new ItemFilterDto();
        dto3.setName("Laptop");
        dto3.setMinPrice(BigDecimal.valueOf(200));
        dto3.setMaxPrice(BigDecimal.valueOf(2000));

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
        ItemFilterDto dto = new ItemFilterDto();

        dto.setName("Phone");
        dto.setMinPrice(BigDecimal.valueOf(100));
        dto.setMaxPrice(BigDecimal.valueOf(1000));

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("name=Phone"));
        assertTrue(result.contains("minPrice=100"));
        assertTrue(result.contains("maxPrice=1000"));
    }
}