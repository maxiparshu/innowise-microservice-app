package by.innowise.course.mapper;

import by.innowise.course.dto.PaymentCardRequestDto;
import by.innowise.course.dto.PaymentCardResponseDto;
import by.innowise.course.entity.PaymentCard;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-24T08:35:49+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-java-compiler-worker-9.5.1.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class PaymentCardMapperImpl implements PaymentCardMapper {

    @Override
    public PaymentCard toEntity(PaymentCardRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        PaymentCard paymentCard = new PaymentCard();

        paymentCard.setNumber( dto.getNumber() );
        paymentCard.setHolder( dto.getHolder() );
        paymentCard.setExpirationDate( dto.getExpirationDate() );
        paymentCard.setActive( dto.getActive() );

        return paymentCard;
    }

    @Override
    public PaymentCardResponseDto toDto(PaymentCard card) {
        if ( card == null ) {
            return null;
        }

        PaymentCardResponseDto paymentCardResponseDto = new PaymentCardResponseDto();

        paymentCardResponseDto.setId( card.getId() );
        paymentCardResponseDto.setNumber( card.getNumber() );
        paymentCardResponseDto.setHolder( card.getHolder() );
        paymentCardResponseDto.setExpirationDate( card.getExpirationDate() );
        paymentCardResponseDto.setActive( card.getActive() );
        paymentCardResponseDto.setCreatedAt( card.getCreatedAt() );
        paymentCardResponseDto.setUpdatedAt( card.getUpdatedAt() );

        return paymentCardResponseDto;
    }
}
