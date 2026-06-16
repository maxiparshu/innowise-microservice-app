package by.innowise.course.dto.order;

import by.innowise.course.entity.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderFilterDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<OrderStatus> statuses;
}