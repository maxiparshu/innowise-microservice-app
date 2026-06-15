package by.innowise.course.dto.order;


import by.innowise.course.dto.order.OrderUserDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderUserDtoTest {

    @Test
    void gettersAndSettersShouldWork() {
        OrderUserDto dto = new OrderUserDto();

        LocalDate birthDate = LocalDate.of(2000, Month.JANUARY, 1);

        dto.setId(1L);
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setBirthDate(birthDate);
        dto.setEmail("john.doe@test.com");

        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getName());
        assertEquals("Doe", dto.getSurname());
        assertEquals(birthDate, dto.getBirthDate());
        assertEquals("john.doe@test.com", dto.getEmail());
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        LocalDate birthDate = LocalDate.of(2000,  Month.JANUARY, 1);

        OrderUserDto dto1 = new OrderUserDto();
        dto1.setId(1L);
        dto1.setName("John");
        dto1.setSurname("Doe");
        dto1.setBirthDate(birthDate);
        dto1.setEmail("john.doe@test.com");

        OrderUserDto dto2 = new OrderUserDto();
        dto2.setId(1L);
        dto2.setName("John");
        dto2.setSurname("Doe");
        dto2.setBirthDate(birthDate);
        dto2.setEmail("john.doe@test.com");

        OrderUserDto dto3 = new OrderUserDto();
        dto3.setId(2L);
        dto3.setName("Jane");
        dto3.setSurname("Smith");
        dto3.setBirthDate(birthDate);
        dto3.setEmail("jane@test.com");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());

        assertNotEquals(null, dto1);
        assertNotEquals("string", dto1);

        assertEquals(dto1, dto1);
    }

    @Test
    void toStringShouldContainFields() {
        OrderUserDto dto = new OrderUserDto();

        dto.setId(1L);
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setBirthDate(LocalDate.of(2000,  Month.JANUARY, 1));
        dto.setEmail("john.doe@test.com");

        String result = dto.toString();

        assertNotNull(result);
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name=John"));
        assertTrue(result.contains("surname=Doe"));
        assertTrue(result.contains("birthDate=2000-01-01"));
        assertTrue(result.contains("email=john.doe@test.com"));
    }
}