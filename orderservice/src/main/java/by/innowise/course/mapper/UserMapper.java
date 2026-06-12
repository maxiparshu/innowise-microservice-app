package by.innowise.course.mapper;

import by.innowise.course.client.OrderUserDto;
import by.innowise.course.dto.order.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toResponseDto(OrderUserDto dto);
}