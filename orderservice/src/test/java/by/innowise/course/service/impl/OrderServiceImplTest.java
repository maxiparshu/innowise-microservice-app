package by.innowise.course.service.impl;

import by.innowise.course.client.OrderUserDto;
import by.innowise.course.client.UserFacade;
import by.innowise.course.dto.order.OrderFilterDto;
import by.innowise.course.dto.order.OrderRequestDto;
import by.innowise.course.dto.order.OrderResponseDto;
import by.innowise.course.dto.order.OrderUpdateRequestDto;
import by.innowise.course.dto.order.UserResponseDto;
import by.innowise.course.dto.orderitem.OrderItemRequestDto;
import by.innowise.course.entity.Item;
import by.innowise.course.entity.Order;
import by.innowise.course.entity.OrderStatus;
import by.innowise.course.mapper.OrderMapper;
import by.innowise.course.mapper.UserMapper;
import by.innowise.course.repository.ItemRepository;
import by.innowise.course.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserFacade userFacade;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createShouldCreateOrder() {
        Long userId = 1L;
        Long itemId = 10L;

        Item item = new Item();
        item.setId(itemId);
        item.setPrice(BigDecimal.valueOf(100));

        OrderItemRequestDto itemDto = new OrderItemRequestDto();
        itemDto.setItemId(itemId);
        itemDto.setQuantity(2);

        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(userId);
        requestDto.setStatus(OrderStatus.CREATED);
        requestDto.setItems(List.of(itemDto));

        OrderUserDto user = new OrderUserDto();
        user.setId(userId);

        Order savedOrder = new Order();

        OrderResponseDto responseDto = new OrderResponseDto();
        UserResponseDto userResponseDto = new UserResponseDto();

        when(userFacade.getUser(userId)).thenReturn(user);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toResponseDto(savedOrder)).thenReturn(responseDto);
        when(userMapper.toResponseDto(user)).thenReturn(userResponseDto);

        OrderResponseDto result = orderService.create(requestDto);

        assertNotNull(result);

        verify(userFacade).getUser(userId);
        verify(itemRepository).findById(itemId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createShouldThrowWhenItemNotFound() {
        Long itemId = 10L;

        OrderItemRequestDto itemDto = new OrderItemRequestDto();
        itemDto.setItemId(itemId);
        itemDto.setQuantity(2);

        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(1L);
        requestDto.setItems(List.of(itemDto));

        OrderUserDto user = new OrderUserDto();
        user.setId(1L);

        when(userFacade.getUser(1L)).thenReturn(user);
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.create(requestDto));
    }

    @Test
    void getByIdShouldReturnOrder() {
        Long orderId = 1L;

        Order order = new Order();
        order.setUserId(5L);

        OrderUserDto user = new OrderUserDto();
        user.setId(5L);

        OrderResponseDto responseDto = new OrderResponseDto();
        UserResponseDto userResponseDto = new UserResponseDto();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(userFacade.getUser(5L))
                .thenReturn(user);

        when(orderMapper.toResponseDto(order))
                .thenReturn(responseDto);

        when(userMapper.toResponseDto(user))
                .thenReturn(userResponseDto);

        OrderResponseDto result = orderService.getById(orderId);

        assertNotNull(result);
    }

    @Test
    void getByIdShouldThrowWhenOrderNotFound() {
        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getById(1L));
    }

    @Test
    void getOrdersShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);

        Order order = new Order();
        order.setUserId(1L);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setUserId(1L);

        OrderUserDto user = new OrderUserDto();
        user.setId(1L);

        UserResponseDto userResponseDto = new UserResponseDto();

        Page<Order> page = new PageImpl<>(List.of(order));

        when(orderRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(page);

        when(orderMapper.toResponseDto(order))
                .thenReturn(responseDto);

        when(userFacade.getUser(1L))
                .thenReturn(user);

        when(userMapper.toResponseDto(user))
                .thenReturn(userResponseDto);

        OrderFilterDto filterDto = new OrderFilterDto();

        Page<OrderResponseDto> result =
                orderService.getOrders(filterDto, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getByUserIdShouldReturnPage() {
        Long userId = 1L;

        Pageable pageable = PageRequest.of(0, 10);

        Order order = new Order();

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setUserId(userId);

        OrderUserDto user = new OrderUserDto();
        user.setId(userId);

        UserResponseDto userResponseDto = new UserResponseDto();

        Page<Order> page = new PageImpl<>(List.of(order));

        when(orderRepository.findAllByUserId(userId, pageable))
                .thenReturn(page);

        when(orderMapper.toResponseDto(order))
                .thenReturn(responseDto);

        when(userFacade.getUser(userId))
                .thenReturn(user);

        when(userMapper.toResponseDto(user))
                .thenReturn(userResponseDto);

        Page<OrderResponseDto> result =
                orderService.getByUserId(userId, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void updateShouldUpdateOrder() {
        Long orderId = 1L;
        Long userId = 10L;
        Long itemId = 100L;

        Item item = new Item();
        item.setPrice(BigDecimal.valueOf(50));

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderItems(new ArrayList<>());

        OrderItemRequestDto itemDto = new OrderItemRequestDto();
        itemDto.setItemId(itemId);
        itemDto.setQuantity(3);

        OrderUpdateRequestDto requestDto = new OrderUpdateRequestDto();
        requestDto.setStatus(OrderStatus.CANCELLED);
        requestDto.setItems(List.of(itemDto));

        OrderUserDto user = new OrderUserDto();
        user.setId(userId);

        UserResponseDto userResponseDto = new UserResponseDto();
        OrderResponseDto responseDto = new OrderResponseDto();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));

        when(orderRepository.save(order))
                .thenReturn(order);

        when(userFacade.getUser(userId))
                .thenReturn(user);

        when(orderMapper.toResponseDto(order))
                .thenReturn(responseDto);

        when(userMapper.toResponseDto(user))
                .thenReturn(userResponseDto);

        OrderResponseDto result = orderService.update(orderId, requestDto);

        assertNotNull(result);

        verify(orderRepository).save(order);
    }

    @Test
    void updateShouldThrowWhenOrderNotFound() {
        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        OrderUpdateRequestDto requestDto =
                new OrderUpdateRequestDto();

        assertThrows(EntityNotFoundException.class, () -> orderService.update(1L, requestDto));
    }

    @Test
    void updateShouldThrowWhenItemNotFound() {
        Long orderId = 1L;
        Long itemId = 10L;

        Order order = new Order();
        order.setOrderItems(new ArrayList<>());

        OrderItemRequestDto itemDto = new OrderItemRequestDto();
        itemDto.setItemId(itemId);
        itemDto.setQuantity(2);

        OrderUpdateRequestDto requestDto =
                new OrderUpdateRequestDto();

        requestDto.setItems(List.of(itemDto));

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(itemRepository.findById(itemId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.update(orderId, requestDto));
    }

    @Test
    void deleteShouldDeleteOrder() {
        Order order = new Order();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        orderService.delete(1L);

        verify(orderRepository).delete(order);
    }

    @Test
    void deleteShouldThrowWhenOrderNotFound() {
        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.delete(1L));
    }
}