package by.innowise.course.service;

import by.innowise.course.dto.order.OrderUserDto;
import by.innowise.course.client.UserClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFacade {

    private final UserClient userClient;
    private static final String FALLBACK_NAME = "Unknown";
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackUser")
    public OrderUserDto getUser(Long userId) {
        return userClient.getUserById(userId);
    }

    OrderUserDto fallbackUser(Long userId, Throwable ignored) {
        log.warn("Falling back to default user for userId={}", userId, ignored);
        OrderUserDto dto = new OrderUserDto();
        dto.setId(userId);
        dto.setName(FALLBACK_NAME);
        dto.setSurname(FALLBACK_NAME);
        dto.setEmail(FALLBACK_NAME);
        return dto;
    }
}