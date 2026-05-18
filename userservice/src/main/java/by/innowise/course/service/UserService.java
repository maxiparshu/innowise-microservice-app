package by.innowise.course.service;

import by.innowise.course.dto.UserRequestDto;
import by.innowise.course.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {
    UserResponseDto create(UserRequestDto dto);

    UserResponseDto readById(Long id);

    Page<UserResponseDto> readAll(
            String name,
            String surname,
            Pageable pageable
    );

    Page<UserResponseDto> readActiveUsers(Pageable pageable);

    Page<UserResponseDto> readUsersWithoutCards(Pageable pageable);

    UserResponseDto update(
            Long id,
            UserRequestDto dto
    );

    void activate(Long id);

    void deactivate(Long id);

    void delete(Long id);
}
