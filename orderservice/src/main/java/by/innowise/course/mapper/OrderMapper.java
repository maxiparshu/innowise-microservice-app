package by.innowise.course.mapper;

import by.innowise.course.dto.order.OrderResponseDto;
import by.innowise.course.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = OrderItemMapper.class
)
public interface OrderMapper {

    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "user", ignore = true)
    OrderResponseDto toResponseDto(Order order);
}