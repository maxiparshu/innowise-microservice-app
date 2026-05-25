package by.innowise.course.integration;

import by.innowise.course.dto.PaymentCardRequestDto;
import by.innowise.course.dto.UserRequestDto;
import by.innowise.course.utils.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class CardsControllerIntegrationTest {
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

    @Test
    void createCardScenario() throws Exception {

        UserRequestDto request = TestDataFactory.createUserRequestDto();
        request.setEmail("test1@mail.ru");
        String createResponse = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        long userId = objectMapper.readTree(createResponse)
                .get("id")
                .asLong();
        PaymentCardRequestDto cardResponseDto = TestDataFactory.createPaymentCardRequestDto();

        String paymentCardResponseDto = mockMvc.perform(post("/api/v1/users/" + userId + "/payment-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardResponseDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long cardId = objectMapper.readTree(paymentCardResponseDto)
                .get("id")
                .asLong();
        mockMvc.perform(get("/api/v1/cards/" + cardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardId))
                .andExpect(jsonPath("$.number").value(cardResponseDto.getNumber()))
                .andExpect(jsonPath("$.holder").value(cardResponseDto.getHolder()))
                .andExpect(jsonPath("$.expirationDate").value(cardResponseDto.getExpirationDate().toString()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());

        for (int i = 0; i < 4; i++) {
            cardResponseDto.setNumber(new StringBuilder().repeat(String.valueOf(i), 15).toString());
            mockMvc.perform(post("/api/v1/users/" + userId + "/payment-cards")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cardResponseDto)))
                    .andExpect(status().isCreated());
        }
        cardResponseDto.setNumber(new StringBuilder().repeat(String.valueOf(6), 15).toString());
        mockMvc.perform(post("/api/v1/users/" + userId + "/payment-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardResponseDto)))
                .andExpect(status().isConflict());

        mockMvc.perform(get("/api/v1/users/" + userId + "/payment-cards")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(5));

        String existedNumber = new StringBuilder().repeat(String.valueOf(2), 15).toString();
        PaymentCardRequestDto updateCard = TestDataFactory.createPaymentCardRequestDto();
        updateCard.setNumber(existedNumber);

        mockMvc.perform(put("/api/v1/cards/" + cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCard)))
                .andExpect(status().isConflict());

        PaymentCardRequestDto updateCardNormal = TestDataFactory.createPaymentCardRequestDto();
        updateCardNormal.setHolder("123");

        mockMvc.perform(put("/api/v1/cards/" + cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCardNormal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.holder").value(updateCardNormal.getHolder()));

        mockMvc.perform(delete("/api/v1/cards/" + cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCardNormal)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/cards")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(4));
    }

    @Test
    void shouldNotCreateValidationError() throws Exception {
        UserRequestDto userRequest = TestDataFactory.createUserRequestDto();

        String createResponse = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        long userId = objectMapper.readTree(createResponse)
                .get("id")
                .asLong();

        PaymentCardRequestDto request = new PaymentCardRequestDto();

        mockMvc.perform(post("/api/v1/users/" + userId + "/payment-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request = TestDataFactory.createPaymentCardRequestDto();
        request.setNumber("fffff");
        mockMvc.perform(post("/api/v1/users/" + userId + "/payment-cards")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request.setNumber("111122223333444455555");
        mockMvc.perform(post("/api/v1/users/" + userId + "/payment-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request.setHolder("");
        mockMvc.perform(post("/api/v1/users/" + userId + "/payment-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldNotFindCard() throws Exception {
        mockMvc.perform(get("/api/v1/cards/" + 2222))
                .andExpect(status().isNotFound());

        PaymentCardRequestDto request = TestDataFactory.createPaymentCardRequestDto();
        mockMvc.perform(put("/api/v1/cards/" + 2222)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/v1/cards/" + 2222)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
        mockMvc.perform(patch("/api/v1/cards/" + 2222 + "/deactivate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        mockMvc.perform(patch("/api/v1/cards/" + 2222 + "/activate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
