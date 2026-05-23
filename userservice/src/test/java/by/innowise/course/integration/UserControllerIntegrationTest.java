package by.innowise.course.integration;

import by.innowise.course.dto.UserRequestDto;
import by.innowise.course.utils.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest  {
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @Container
    static GenericContainer<?> redis =
            new GenericContainer<>("redis:7")
                    .withExposedPorts(6379);

    static {
        postgres.start();
        redis.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port",
                () -> redis.getMappedPort(6379));
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void successfulUserScenario() throws Exception {

        UserRequestDto request = TestDataFactory.createUserRequestDto();
        request.setEmail("test@mail.ru");

        String createResponse = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long userId = objectMapper.readTree(createResponse)
                .get("id")
                .asLong();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.surname").value(request.getSurname()))
                .andExpect(jsonPath("$.birthDate").value(request.getBirthDate().toString()))
                .andExpect(jsonPath("$.cards").isArray())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());

        UserRequestDto update = new UserRequestDto();
        update.setName("Updated");
        update.setSurname("User");
        update.setBirthDate(request.getBirthDate());
        update.setEmail("updated@mail.com");

        mockMvc.perform(put("/api/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@mail.com"));

        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@mail.com"));

        String redisKey = "users::" + userId;

        Boolean exists = redisTemplate.hasKey(redisKey);

        Assertions.assertEquals(Boolean.TRUE, exists);

        String cachedValue = redisTemplate.opsForValue().get(redisKey);

        Assertions.assertNotNull(
                cachedValue
        );
        mockMvc.perform(patch("/api/v1/users/" + userId + "/deactivate"))
                .andExpect(status().isNoContent());

        mockMvc.perform(patch("/api/v1/users/" + userId + "/activate"))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotCreateUserValidationError() throws Exception {
        UserRequestDto request = new UserRequestDto();
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request.setName("name");
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request.setSurname("surname");
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request.setBirthDate(LocalDate.now());
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldNotFindUser() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + 2222))
                .andExpect(status().isNotFound());

        UserRequestDto request = TestDataFactory.createUserRequestDto();
        mockMvc.perform(put("/api/v1/users/" + 2222)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/v1/users/" + 2222)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
        mockMvc.perform(patch("/api/v1/users/" + 2222 + "/deactivate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        mockMvc.perform(patch("/api/v1/users/" + 2222 + "/activate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}