package by.innowise.course.service.impl;

import by.innowise.course.dto.UserRequestDto;
import by.innowise.course.dto.UserResponseDto;
import by.innowise.course.entity.User;
import by.innowise.course.exception.UserNotFoundException;
import by.innowise.course.exception.UserWithEmailAlreadyExistException;
import by.innowise.course.mapper.UserMapper;
import by.innowise.course.repository.UserRepository;
import by.innowise.course.service.impl.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void shouldCreateUser() {
        UserRequestDto dto = TestDataFactory.createUserRequestDto();
        User user = new User();
        User saved = TestDataFactory.createUser();
        UserResponseDto response = TestDataFactory.createUserResponseDto();

        when(userRepository.existsByEmail(dto.getEmail()))
                .thenReturn(false);
        when(userMapper.toEntity(dto))
                .thenReturn(user);
        when(userRepository.save(user))
                .thenReturn(saved);
        when(userMapper.toDto(saved))
                .thenReturn(response);

        UserResponseDto result = userService.create(dto);

        assertNotNull(result);
        assertEquals(response, result);
        verify(userRepository).save(user);
    }

    @Test
    public void shouldThrowWhenEmailExists() {
        UserRequestDto dto = new UserRequestDto();
        dto.setEmail("test@mail.com");

        when(userRepository.existsByEmail(dto.getEmail()))
                .thenReturn(true);

        assertThrows(
                UserWithEmailAlreadyExistException.class,
                () -> userService.create(dto)
        );
    }


    @Test
    public void shouldReturnUserById() {
        User user = new User();
        UserResponseDto dto = TestDataFactory.createUserResponseDto();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userMapper.toDto(user))
                .thenReturn(dto);

        UserResponseDto result = userService
                .readById(1L);

        assertNotNull(result);
        assertEquals(dto, result);
        verify(userRepository)
                .findById(1L);
    }

    @Test
    public void shouldThrowWhenUserNotFound() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.readById(1L)
        );
    }


    @Test
    public void shouldUpdateUserWithoutEmailConflict() {
        UserRequestDto request = TestDataFactory.createUserRequestDto();
        User user = TestDataFactory.createUser();
        UserResponseDto response = TestDataFactory.createUserResponseDto();
        response.setName("new name");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user))
                .thenReturn(user);
        when(userMapper.toDto(user))
                .thenReturn(response);

        UserResponseDto result = userService
                .update(1L, request);

        assertNotNull(result);
        assertEquals(result, response);
        verify(userRepository, never()).existsByEmail(any());
    }

    @Test
    public void shouldThrowWhenEmailChangedAndAlreadyExists() {
        UserRequestDto dto = new UserRequestDto();
        dto.setEmail("new@mail.com");

        User user = new User();
        user.setEmail("old@mail.com");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@mail.com"))
                .thenReturn(true);

        assertThrows(
                UserWithEmailAlreadyExistException.class,
                () -> userService.update(1L, dto)
        );
    }

    @Test
    public void shouldThrowWhenUserNotFoundInUpdate() {
        UserRequestDto dto = new UserRequestDto();

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.update(1L, dto)
        );
    }

    @Test
    public void shouldReadAllUsersWithSpecification() {
        Pageable pageable = PageRequest.of(0, 10);

        User user = TestDataFactory.createUser();
        UserResponseDto dto = TestDataFactory.createUserResponseDto();

        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findAll(
                ArgumentMatchers.<Specification<User>>any(),
                eq(pageable)
        )).thenReturn(page);

        when(userMapper.toDto(user))
                .thenReturn(dto);

        Page<UserResponseDto> result =
                userService.readAll("", "", pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(dto, result.getContent().getFirst());
    }


    @Test
    public void shouldReadActiveUsers() {
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User();
        UserResponseDto dto = new UserResponseDto();

        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findActiveUsers(pageable))
                .thenReturn(page);

        when(userMapper.toDto(user))
                .thenReturn(dto);

        Page<UserResponseDto> result =
                userService.readActiveUsers(pageable);

        assertNotNull(result);

        verify(userRepository).findActiveUsers(pageable);
    }


    @Test
    public void shouldReadUsersWithoutCards() {

        Pageable pageable = PageRequest.of(0, 10);

        User user = new User();
        UserResponseDto dto = new UserResponseDto();

        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findUsersWithoutCards(pageable))
                .thenReturn(page);

        when(userMapper.toDto(user))
                .thenReturn(dto);

        Page<UserResponseDto> result =
                userService.readUsersWithoutCards(pageable);

        assertNotNull(result);
        verify(userRepository).findUsersWithoutCards(pageable);
    }

    @Test
    public void shouldActivateUser() {
        User user = new User();
        user.setActive(false);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user))
                .thenReturn(user);
        when(userMapper.toDto(user))
                .thenReturn(new UserResponseDto());

        userService.activate(1L);

        verify(userRepository).save(user);
    }

    @Test
    public void shouldThrowWhenUserNotFoundInActivate() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.activate(1L)
        );
    }

    @Test
    public void shouldDeactivateUser() {
        User user = new User();
        user.setActive(true);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(userRepository.save(user))
                .thenReturn(user);
        when(userMapper.toDto(user))
                .thenReturn(new UserResponseDto());

        userService.deactivate(1L);

        verify(userRepository).save(user);
    }

    @Test
    public void shouldThrowWhenUserNotFoundInDeactivate() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.deactivate(1L)
        );
    }

    @Test
    void shouldDeleteUser() {
        User user = new User();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenUserNotFoundInDelete() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.delete(1L)
        );
    }

}