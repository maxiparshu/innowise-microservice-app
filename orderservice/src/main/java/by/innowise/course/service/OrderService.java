package by.innowise.course.service;


import by.innowise.course.dto.order.OrderFilterDto;
import by.innowise.course.dto.order.OrderRequestDto;
import by.innowise.course.dto.order.OrderResponseDto;
import by.innowise.course.dto.order.OrderUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto create(OrderRequestDto requestDto);

    OrderResponseDto getById(Long id);

    Page<OrderResponseDto> getOrders(
            OrderFilterDto filterDto,
            Pageable pageable
    );

    Page<OrderResponseDto> getByUserId(
            Long userId,
            Pageable pageable
    );

    OrderResponseDto update(
            Long id,
            OrderUpdateRequestDto requestDto
    );

    void delete(Long id);
}
