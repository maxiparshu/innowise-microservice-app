package by.innowise.course.client;

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

    public OrderUserDto fallbackUser(Long userId, Throwable ex) {
        OrderUserDto dto = new OrderUserDto();
        dto.setId(userId);
        dto.setName("Unknown");
        return dto;
    }
}