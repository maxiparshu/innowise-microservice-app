package by.innowise.course.mapper;

import by.innowise.course.dto.order.OrderUserDto;
import by.innowise.course.dto.order.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toResponseDto(OrderUserDto dto);
}