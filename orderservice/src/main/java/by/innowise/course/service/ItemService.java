package by.innowise.course.service;


import by.innowise.course.dto.item.ItemFilterDto;
import by.innowise.course.dto.item.ItemRequestDto;
import by.innowise.course.dto.item.ItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemService {

    ItemResponseDto create(ItemRequestDto requestDto);

    ItemResponseDto getById(Long id);

    Page<ItemResponseDto> getItems(
            ItemFilterDto filterDto,
            Pageable pageable
    );

    ItemResponseDto update(
            Long id,
            ItemRequestDto requestDto
    );

    void delete(Long id);
}
