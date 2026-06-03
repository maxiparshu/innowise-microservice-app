package by.innowise.course.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

class AccessServiceTest {

    @InjectMocks
    private AccessService accessService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isOwner_ShouldReturnTrue_WhenUserIsOwner() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(100L);

            boolean result = accessService.isOwner(100L);

            assertTrue(result);
        }
    }

    @Test
    void isOwner_ShouldReturnFalse_WhenUserIsNotOwner() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(100L);

            boolean result = accessService.isOwner(200L);

            assertFalse(result);
        }
    }
}