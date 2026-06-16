package by.innowise.course.mapper;

import by.innowise.course.dto.orderitem.OrderItemResponseDto;
import by.innowise.course.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "itemName", source = "item.name")
    @Mapping(target = "itemPrice", source = "item.price")
    OrderItemResponseDto toResponseDto(OrderItem entity);
}