package by.innowise.course.mapper;

import by.innowise.course.dto.PaymentCardResponseDto;
import by.innowise.course.dto.UserRequestDto;
import by.innowise.course.dto.UserResponseDto;
import by.innowise.course.entity.PaymentCard;
import by.innowise.course.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-24T08:35:49+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-java-compiler-worker-9.5.1.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private PaymentCardMapper paymentCardMapper;

    @Override
    public User toEntity(UserRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setName( dto.getName() );
        user.setSurname( dto.getSurname() );
        user.setBirthDate( dto.getBirthDate() );
        user.setEmail( dto.getEmail() );

        return user;
    }

    @Override
    public UserResponseDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setCards( paymentCardListToPaymentCardResponseDtoList( user.getPaymentCards() ) );
        userResponseDto.setId( user.getId() );
        userResponseDto.setName( user.getName() );
        userResponseDto.setSurname( user.getSurname() );
        userResponseDto.setBirthDate( user.getBirthDate() );
        userResponseDto.setEmail( user.getEmail() );
        userResponseDto.setActive( user.getActive() );
        userResponseDto.setCreatedAt( user.getCreatedAt() );
        userResponseDto.setUpdatedAt( user.getUpdatedAt() );

        return userResponseDto;
    }

    protected List<PaymentCardResponseDto> paymentCardListToPaymentCardResponseDtoList(List<PaymentCard> list) {
        if ( list == null ) {
            return null;
        }

        List<PaymentCardResponseDto> list1 = new ArrayList<PaymentCardResponseDto>( list.size() );
        for ( PaymentCard paymentCard : list ) {
            list1.add( paymentCardMapper.toDto( paymentCard ) );
        }

        return list1;
    }
}
