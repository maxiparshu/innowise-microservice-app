package by.innowise.course.service.impl;

import by.innowise.course.dto.UserRequestDto;
import by.innowise.course.dto.UserResponseDto;
import by.innowise.course.entity.User;
import by.innowise.course.exception.UserNotFoundException;
import by.innowise.course.exception.UserWithEmailAlreadyExistException;
import by.innowise.course.mapper.UserMapper;
import by.innowise.course.repository.UserRepository;
import by.innowise.course.service.UserService;
import by.innowise.course.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto dto) {
        String email = dto.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new UserWithEmailAlreadyExistException(email);
        }
        User user = userMapper.toEntity(dto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Cacheable(
            value = "users",
            key = "#id"
    )
    public UserResponseDto readById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException(id)
        );
        return userMapper.toDto(user);
    }

    @Override
    public Page<UserResponseDto> readAll(
            String name,
            String surname,
            Pageable pageable
    ) {

        Specification<User> specification =
                Specification.where(UserSpecification.hasName(name))
                        .and(UserSpecification.hasSurname(surname));

        return userRepository.findAll(specification, pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserResponseDto> readActiveUsers(Pageable pageable) {
        return userRepository.findActiveUsers(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserResponseDto> readUsersWithoutCards(Pageable pageable) {
        return userRepository.findUsersWithoutCards(pageable)
                .map(userMapper::toDto);
    }

    @Override
    @Transactional
    @CachePut(
            value = "users",
            key = "#id"
    )
    public UserResponseDto update(
            Long id,
            UserRequestDto dto
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(()
                        -> new UserNotFoundException(id)
                );
        if (!user.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new UserWithEmailAlreadyExistException(dto.getEmail());
        }
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setBirthDate(dto.getBirthDate());
        user.setEmail(dto.getEmail());


        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    @CachePut(
            value = "users",
            key = "#id"
    )
    public void activate(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(()
                        -> new UserNotFoundException(id)
                );

        user.setActive(true);
        userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    @CachePut(
            value = "users",
            key = "#id"
    )
    public void deactivate(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(()
                        -> new UserNotFoundException(id)
                );

        user.setActive(false);
        userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    @CacheEvict(
            value = "users",
            key = "#id"
    )
    public void delete(Long id) {
        userRepository.findById(id)
                .orElseThrow(()
                        -> new UserNotFoundException(id)
                );
        userRepository.deleteById(id);
    }

}
