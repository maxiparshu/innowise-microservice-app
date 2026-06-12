package by.innowise.course.client;

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

    @Test
    void fallbackUserShouldReturnUnknownUser() {
        Long userId = 1L;

        OrderUserDto result =
                userFacade.fallbackUser(userId, new RuntimeException());

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Unknown", result.getName());

        verifyNoInteractions(userClient);
    }
}