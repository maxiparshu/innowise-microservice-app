package by.innowise.course.mapper;


import by.innowise.course.dto.item.ItemRequestDto;
import by.innowise.course.dto.item.ItemResponseDto;
import by.innowise.course.entity.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item toEntity(ItemRequestDto dto);

    ItemResponseDto toResponseDto(Item item);
}