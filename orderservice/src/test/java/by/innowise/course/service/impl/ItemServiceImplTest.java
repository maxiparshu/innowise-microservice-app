package by.innowise.course.service.impl;

import by.innowise.course.dto.item.ItemFilterDto;
import by.innowise.course.dto.item.ItemRequestDto;
import by.innowise.course.dto.item.ItemResponseDto;
import by.innowise.course.entity.Item;
import by.innowise.course.mapper.ItemMapper;
import by.innowise.course.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void createShouldSaveItem() {
        ItemRequestDto requestDto = new ItemRequestDto();
        Item item = new Item();
        Item savedItem = new Item();
        ItemResponseDto responseDto = new ItemResponseDto();

        when(itemMapper.toEntity(requestDto)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(savedItem);
        when(itemMapper.toResponseDto(savedItem)).thenReturn(responseDto);

        ItemResponseDto result = itemService.create(requestDto);

        assertEquals(responseDto, result);

        verify(itemMapper).toEntity(requestDto);
        verify(itemRepository).save(item);
        verify(itemMapper).toResponseDto(savedItem);
    }

    @Test
    void getByIdShouldReturnItem() {
        Long id = 1L;

        Item item = new Item();
        ItemResponseDto responseDto = new ItemResponseDto();

        when(itemRepository.findById(id))
                .thenReturn(Optional.of(item));

        when(itemMapper.toResponseDto(item))
                .thenReturn(responseDto);

        ItemResponseDto result = itemService.getById(id);

        assertEquals(responseDto, result);

        verify(itemRepository).findById(id);
        verify(itemMapper).toResponseDto(item);
    }

    @Test
    void getByIdShouldThrowExceptionWhenItemNotFound() {
        Long id = 1L;

        when(itemRepository.findById(id))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> itemService.getById(id)
        );

        assertEquals(
                "Item with id 1 not found",
                exception.getMessage()
        );
    }

    @Test
    void getItemsShouldReturnPage() {
        ItemFilterDto filterDto = new ItemFilterDto();
        filterDto.setName("phone");
        filterDto.setMinPrice(BigDecimal.valueOf(100));
        filterDto.setMaxPrice(BigDecimal.valueOf(500));

        Pageable pageable = PageRequest.of(0, 10);

        Item item = new Item();
        ItemResponseDto responseDto = new ItemResponseDto();

        Page<Item> itemPage = new PageImpl<>(List.of(item));

        when(itemRepository.findAll(
                ArgumentMatchers.<Specification<Item>>any(),
                eq(pageable)
        )).thenReturn(itemPage);

        when(itemMapper.toResponseDto(item))
                .thenReturn(responseDto);

        Page<ItemResponseDto> result =
                itemService.getItems(filterDto, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(responseDto, result.getContent().getFirst());
    }

    @Test
    void updateShouldUpdateItem() {
        Long id = 1L;

        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setName("Updated");
        requestDto.setPrice(BigDecimal.valueOf(250));

        Item item = new Item();
        Item updatedItem = new Item();

        ItemResponseDto responseDto = new ItemResponseDto();

        when(itemRepository.findById(id))
                .thenReturn(Optional.of(item));

        when(itemRepository.save(item))
                .thenReturn(updatedItem);

        when(itemMapper.toResponseDto(updatedItem))
                .thenReturn(responseDto);

        ItemResponseDto result =
                itemService.update(id, requestDto);

        assertEquals(responseDto, result);

        assertEquals("Updated", item.getName());
        assertEquals(BigDecimal.valueOf(250), item.getPrice());

        verify(itemRepository).findById(id);
        verify(itemRepository).save(item);
        verify(itemMapper).toResponseDto(updatedItem);
    }

    @Test
    void updateShouldThrowExceptionWhenItemNotFound() {
        Long id = 1L;

        when(itemRepository.findById(id))
                .thenReturn(Optional.empty());

        ItemRequestDto requestDto = new ItemRequestDto();

        assertThrows(EntityNotFoundException.class, () -> itemService.update(id, requestDto));
    }

    @Test
    void deleteShouldDeleteItem() {
        Long id = 1L;

        Item item = new Item();

        when(itemRepository.findById(id))
                .thenReturn(Optional.of(item));

        itemService.delete(id);

        verify(itemRepository).findById(id);
        verify(itemRepository).delete(item);
    }

    @Test
    void deleteShouldThrowExceptionWhenItemNotFound() {
        Long id = 1L;

        when(itemRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.delete(id));
    }
}