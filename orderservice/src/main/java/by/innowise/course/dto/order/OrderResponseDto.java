package by.innowise.course.dto.order;

import by.innowise.course.client.OrderUserDto;
import by.innowise.course.dto.orderitem.OrderItemResponseDto;
import by.innowise.course.entity.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private UserResponseDto user;
    private List<OrderItemResponseDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}