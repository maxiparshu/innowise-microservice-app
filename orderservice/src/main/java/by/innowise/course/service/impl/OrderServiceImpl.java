package by.innowise.course.service.impl;

import by.innowise.course.dto.order.OrderFilterDto;
import by.innowise.course.dto.order.OrderRequestDto;
import by.innowise.course.dto.order.OrderResponseDto;
import by.innowise.course.dto.order.OrderUpdateRequestDto;
import by.innowise.course.dto.order.OrderUserDto;
import by.innowise.course.dto.order.UserResponseDto;
import by.innowise.course.dto.orderitem.OrderItemRequestDto;
import by.innowise.course.entity.Item;
import by.innowise.course.entity.Order;
import by.innowise.course.entity.OrderItem;
import by.innowise.course.mapper.OrderMapper;
import by.innowise.course.mapper.UserMapper;
import by.innowise.course.repository.ItemRepository;
import by.innowise.course.repository.OrderRepository;
import by.innowise.course.service.OrderService;
import by.innowise.course.service.UserFacade;
import by.innowise.course.specification.OrderSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final UserFacade userFacade;

    private static final String NOT_FOUND_ORDER_MESSAGE = "Order with id %d not found";

    @Override
    @Transactional
    public OrderResponseDto create(OrderRequestDto requestDto) {
        OrderUserDto user = userFacade.getUser(requestDto.getUserId());
        Order order = new Order();

        order.setUserId(user.getId());
        order.setStatus(requestDto.getStatus());
        order.setOrderItems(new ArrayList<>());

        setOrders(requestDto.getItems(), order);

        Order saved = orderRepository.save(order);

        OrderResponseDto responseDto = orderMapper.toResponseDto(saved);
        responseDto.setUser(userMapper.toResponseDto(user));

        return responseDto;
    }


    @Override
    public OrderResponseDto getById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_ORDER_MESSAGE.formatted(id)));

        OrderUserDto user = userFacade.getUser(order.getUserId());
        OrderResponseDto responseDto = orderMapper.toResponseDto(order);
        responseDto.setUser(userMapper.toResponseDto(user));

        return responseDto;
    }

    @Override
    public Page<OrderResponseDto> getOrders(
            OrderFilterDto filterDto,
            Pageable pageable
    ) {

        Specification<Order> spec = Specification
                .where(OrderSpecification.betweenDates(
                        filterDto.getStartDate(),
                        filterDto.getEndDate()
                ))
                .and(OrderSpecification.hasStatuses(
                        filterDto.getStatuses()
                ));

        Map<Long, UserResponseDto> userCache = new HashMap<>();

        return orderRepository.findAll(spec, pageable)
                .map(order -> {
                    OrderResponseDto dto = orderMapper.toResponseDto(order);

                    UserResponseDto user = userCache.computeIfAbsent(
                            dto.getUserId(),
                            userId -> userMapper.toResponseDto(
                                    userFacade.getUser(userId)
                            )
                    );

                    dto.setUser(user);
                    return dto;
                });
    }

    @Override
    public Page<OrderResponseDto> getByUserId(
            Long userId,
            Pageable pageable
    ) {

        return orderRepository.findAllByUserId(userId, pageable)
                .map(order -> {
                    OrderResponseDto dto = orderMapper.toResponseDto(order);

                    OrderUserDto user = userFacade.getUser(dto.getUserId());
                    dto.setUser(userMapper.toResponseDto(user));

                    return dto;
                });
    }

    @Override
    @Transactional
    public OrderResponseDto update(
            Long id,
            OrderUpdateRequestDto requestDto
    ) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_ORDER_MESSAGE.formatted(id)));

        order.setStatus(requestDto.getStatus());
        order.getOrderItems().clear();
        setOrders(requestDto.getItems(), order);
        orderRepository.save(order);

        OrderUserDto user = userFacade.getUser(order.getUserId());
        OrderResponseDto responseDto = orderMapper.toResponseDto(order);
        responseDto.setUser(userMapper.toResponseDto(user));

        return responseDto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_ORDER_MESSAGE.formatted(id)));

        orderRepository.delete(order);
    }

    private void setOrders(List<OrderItemRequestDto> requestDto, Order order) {
        if (requestDto == null) {
            return;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDto dto : requestDto) {
            Item item = itemRepository.findById(dto.getItemId())
                    .orElseThrow(() -> new EntityNotFoundException("Item not found: " + dto.getItemId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItem(item);
            orderItem.setQuantity(dto.getQuantity());

            order.getOrderItems().add(orderItem);

            total = total.add(
                    item.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity()))
            );
        }

        order.setTotalPrice(total);
    }

}