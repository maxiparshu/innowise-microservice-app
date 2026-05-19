package by.innowise.course.contoller;

import by.innowise.course.dto.UserRequestDto;
import by.innowise.course.dto.UserResponseDto;
import by.innowise.course.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(
            @Valid @RequestBody UserRequestDto dto
    ) {

        return userService.create(dto);
    }

    @GetMapping("/{id}")
    public UserResponseDto readById(
            @Positive(message = "Id must be positive")
            @PathVariable Long id
    ) {

        return userService.readById(id);
    }

    @GetMapping
    public Page<UserResponseDto> readAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return userService.readAll(
                name,
                surname,
                PageRequest.of(page, size)
        );
    }

    @GetMapping("/active")
    public Page<UserResponseDto> readActiveUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return userService.readActiveUsers(
                PageRequest.of(page, size)
        );
    }

    @GetMapping("/without-cards")
    public Page<UserResponseDto> readUsersWithoutCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return userService.readUsersWithoutCards(
                PageRequest.of(page, size)
        );
    }

    @PutMapping("/{id}")
    public UserResponseDto update(
            @Positive(message = "Id must be positive")
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto dto
    ) {

        return userService.update(id, dto);
    }

    @PatchMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(
            @Positive(message = "Id must be positive")
            @PathVariable Long id
    ) {

        userService.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(
            @Positive(message = "Id must be positive")
            @PathVariable Long id
    ) {

        userService.deactivate(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Positive(message = "Id must be positive")
            @PathVariable Long id
    ) {

        userService.delete(id);
    }
}