package by.innowise.course.mapper;

import by.innowise.course.dto.PaymentCardRequestDto;
import by.innowise.course.dto.PaymentCardResponseDto;
import by.innowise.course.entity.PaymentCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentCardMapper {

    PaymentCard toEntity(PaymentCardRequestDto dto);

    PaymentCardResponseDto toDto(PaymentCard card);
}
