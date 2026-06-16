package by.innowise.course.service.impl;

import by.innowise.course.client.UserClient;
import by.innowise.course.dto.order.OrderUserDto;
import by.innowise.course.service.UserFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserFacade userFacade;

    @Test
    void getUserShouldReturnUser() {
        Long userId = 1L;

        OrderUserDto user = new OrderUserDto();
        user.setId(userId);

        when(userClient.getUserById(userId))
                .thenReturn(user);

        OrderUserDto result = userFacade.getUser(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());

        verify(userClient).getUserById(userId);
    }
}