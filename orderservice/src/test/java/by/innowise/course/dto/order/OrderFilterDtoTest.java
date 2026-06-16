package by.innowise.course.dto.order;

import by.innowise.course.entity.OrderStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderFilterDtoTest {

    @Test
    void gettersAndSettersShouldWork() {
        OrderFilterDto dto = new OrderFilterDto();

        LocalDate startDate = LocalDate.of(2025, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2025,  Month.DECEMBER, 31);
        List<OrderStatus> statuses =
                List.of(OrderStatus.CREATED, OrderStatus.DELIVERED);

        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setStatuses(statuses);

        assertEquals(startDate, dto.getStartDate());
        assertEquals(endDate, dto.getEndDate());
        assertEquals(statuses, dto.getStatuses());
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        List<OrderStatus> statuses = List.of(OrderStatus.CREATED, OrderStatus.DELIVERED);

        OrderFilterDto dto1 = new OrderFilterDto();
        dto1.setStartDate(LocalDate.of(2025,  Month.JANUARY, 1));
        dto1.setEndDate(LocalDate.of(2025, Month.DECEMBER, 31));
        dto1.setStatuses(statuses);

        OrderFilterDto dto2 = new OrderFilterDto();
        dto2.setStartDate(LocalDate.of(2025, Month.JANUARY, 1));
        dto2.setEndDate(LocalDate.of(2025, Month.DECEMBER, 31));
        dto2.setStatuses(statuses);

        OrderFilterDto dto3 = new OrderFilterDto();
        dto3.setStartDate(LocalDate.of(2024, Month.JANUARY, 1));
        dto3.setEndDate(LocalDate.of(2024, Month.DECEMBER, 31));
        dto3.setStatuses(List.of(OrderStatus.CANCELLED));

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
        OrderFilterDto dto = new OrderFilterDto();

        dto.setStartDate(LocalDate.of(2025, Month.JANUARY, 1));
        dto.setEndDate(LocalDate.of(2025, Month.DECEMBER, 31));
        dto.setStatuses(List.of(OrderStatus.CREATED));

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("startDate=2025-01-01"));
        assertTrue(result.contains("endDate=2025-12-31"));
        assertTrue(result.contains("statuses=[CREATED]"));
    }
}