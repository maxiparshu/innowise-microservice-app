package by.innowise.course.dto.order;


import by.innowise.course.dto.orderitem.OrderItemRequestDto;
import by.innowise.course.entity.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private OrderStatus status;

    @Valid
    @NotEmpty
    private List<OrderItemRequestDto> items;
}
