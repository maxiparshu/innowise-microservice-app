package by.innowise.course.service.impl;

import by.innowise.course.dto.item.ItemFilterDto;
import by.innowise.course.dto.item.ItemRequestDto;
import by.innowise.course.dto.item.ItemResponseDto;
import by.innowise.course.entity.Item;
import by.innowise.course.mapper.ItemMapper;
import by.innowise.course.repository.ItemRepository;
import by.innowise.course.service.ItemService;
import by.innowise.course.specification.ItemSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemResponseDto create(ItemRequestDto requestDto) {
        Item item = itemMapper.toEntity(requestDto);

        Item savedItem = itemRepository.save(item);

        return itemMapper.toResponseDto(savedItem);
    }

    @Override
    public ItemResponseDto getById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Item with id %d not found".formatted(id))
                );

        return itemMapper.toResponseDto(item);
    }

    @Override
    public Page<ItemResponseDto> getItems(ItemFilterDto filterDto, Pageable pageable) {
        Specification<Item> specification = Specification.where(
                ItemSpecification.containName(
                        filterDto.getName()
                ))
                .and(
                ItemSpecification.betweenPrice(
                        filterDto.getMinPrice(),
                        filterDto.getMaxPrice()
                ));

        return itemRepository.findAll(specification, pageable)
                .map(itemMapper::toResponseDto);
    }

    @Override
    @Transactional
    public ItemResponseDto update(Long id, ItemRequestDto requestDto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Item with id %d not found".formatted(id))
                );

        item.setName(requestDto.getName());
        item.setPrice(requestDto.getPrice());

        Item updatedItem = itemRepository.save(item);

        return itemMapper.toResponseDto(updatedItem);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Item with id %d not found".formatted(id)
                        ));

        itemRepository.delete(item);
    }
}
