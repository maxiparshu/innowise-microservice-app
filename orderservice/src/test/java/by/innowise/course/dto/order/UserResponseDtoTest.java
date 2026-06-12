package by.innowise.course.dto.order;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDtoTest {

    @Test
    void gettersAndSettersShouldWork() {
        UserResponseDto dto = new UserResponseDto();

        LocalDate birthDate = LocalDate.of(2000, 1, 1);

        dto.setName("John");
        dto.setSurname("Doe");
        dto.setBirthDate(birthDate);
        dto.setEmail("john@test.com");

        assertEquals("John", dto.getName());
        assertEquals("Doe", dto.getSurname());
        assertEquals(birthDate, dto.getBirthDate());
        assertEquals("john@test.com", dto.getEmail());
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);

        UserResponseDto dto1 = new UserResponseDto();
        dto1.setName("John");
        dto1.setSurname("Doe");
        dto1.setBirthDate(birthDate);
        dto1.setEmail("john@test.com");

        UserResponseDto dto2 = new UserResponseDto();
        dto2.setName("John");
        dto2.setSurname("Doe");
        dto2.setBirthDate(birthDate);
        dto2.setEmail("john@test.com");

        UserResponseDto dto3 = new UserResponseDto();
        dto3.setName("Jane");
        dto3.setSurname("Smith");
        dto3.setBirthDate(birthDate);
        dto3.setEmail("jane@test.com");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());

        assertEquals(dto1, dto1);
        assertNotEquals(dto1, null);
        assertNotEquals(dto1, new Object());
    }

    @Test
    void toStringShouldContainFields() {
        UserResponseDto dto = new UserResponseDto();

        dto.setName("John");
        dto.setSurname("Doe");
        dto.setBirthDate(LocalDate.of(2000, 1, 1));
        dto.setEmail("john@test.com");

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("name=John"));
        assertTrue(result.contains("surname=Doe"));
        assertTrue(result.contains("birthDate=2000-01-01"));
        assertTrue(result.contains("email=john@test.com"));
    }
}