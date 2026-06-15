package by.innowise.course.service;

import by.innowise.course.dto.order.OrderUserDto;
import by.innowise.course.client.UserClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserClient userClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackUser")
    public OrderUserDto getUser(Long userId) {
        return userClient.getUserById(userId);
    }

    OrderUserDto fallbackUser(Long userId, Throwable ignored) {
        OrderUserDto dto = new OrderUserDto();
        dto.setId(userId);
        dto.setName("Unknown");
        dto.setSurname("Unknown");
        dto.setEmail("Unknown");
        return dto;
    }
}