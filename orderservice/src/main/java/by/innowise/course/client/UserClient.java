package by.innowise.course.client;

import by.innowise.course.dto.order.OrderUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-service",
        configuration = FeignAuthConfig.class
)
public interface UserClient {

    @GetMapping("/api/v1/users/{id}")
    OrderUserDto getUserById(
            @PathVariable("id") Long id
    );
}