package by.innowise.course.dto.order;


import by.innowise.course.dto.orderitem.OrderItemRequestDto;
import by.innowise.course.entity.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderUpdateRequestDto {

    @NotNull
    private OrderStatus status;

    @Valid
    private List<OrderItemRequestDto> items;
}