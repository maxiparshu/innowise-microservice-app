package by.innowise.course.mapper;

import by.innowise.course.dto.UserRequestDto;
import by.innowise.course.dto.UserResponseDto;
import by.innowise.course.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = PaymentCardMapper.class
)
public interface UserMapper {

    User toEntity(UserRequestDto dto);

    @Mapping(target = "cards", source = "paymentCards")
    UserResponseDto toDto(User user);
}