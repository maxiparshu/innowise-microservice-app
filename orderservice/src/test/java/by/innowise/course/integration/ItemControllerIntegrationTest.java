package by.innowise.course.integration;

import by.innowise.course.dto.item.ItemRequestDto;
import by.innowise.course.entity.Item;
import by.innowise.course.repository.ItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ItemControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("jwt.secret",
                () -> "mySuperSecretKeyForJwtTokenGeneration123456");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void createShouldSaveItem() throws Exception {
        itemRepository.deleteAll();

        ItemRequestDto request = new ItemRequestDto();

        mockMvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request.setName("Phone");
        request.setPrice(BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Phone"))
                .andExpect(jsonPath("$.price").value(100));

        assertEquals(1, itemRepository.count());

        Item item = itemRepository.findAll().getFirst();

        assertEquals("Phone", item.getName());
        assertTrue(BigDecimal.valueOf(100).compareTo(item.getPrice()) == 0);
    }

    @Test
    void getByIdShouldReturnItem() throws Exception {
        itemRepository.deleteAll();

        Item item = new Item();
        item.setName("Phone");
        item.setPrice(BigDecimal.valueOf(100));

        item = itemRepository.save(item);

        mockMvc.perform(get("/api/v1/items/{id}", item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value("Phone"));

        mockMvc.perform(get("/api/v1/items/-1"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/items/222", item.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateShouldUpdateItem() throws Exception {
        itemRepository.deleteAll();

        Item item = new Item();
        item.setName("Old");
        item.setPrice(BigDecimal.valueOf(50));

        item = itemRepository.save(item);

        ItemRequestDto request = new ItemRequestDto();
        request.setName("New");
        request.setPrice(BigDecimal.valueOf(50));

        mockMvc.perform(put("/api/v1/items/{id}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New"));

        Item updated = itemRepository.findById(item.getId()).orElseThrow();
        assertEquals("New", updated.getName());

    }

    @Test
    void getItemsShouldReturnPage() throws Exception {
        itemRepository.deleteAll();

        Item item = new Item();
        item.setName("Phone");
        item.setPrice(BigDecimal.valueOf(100));

        itemRepository.save(item);

        mockMvc.perform(get("/api/v1/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Phone"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteShouldRemoveItem() throws Exception {
        itemRepository.deleteAll();

        Item item = new Item();
        item.setName("Phone");
        item.setPrice(BigDecimal.valueOf(100));

        item = itemRepository.save(item);
        mockMvc.perform(delete("/api/v1/items/-1"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/v1/items/2", item.getId()))
                .andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/v1/items/{id}", item.getId()))
                .andExpect(status().isNoContent());

        assertFalse(itemRepository.existsById(item.getId()));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void createShouldReturnWithoutAdminRole() throws Exception {
        itemRepository.deleteAll();

        ItemRequestDto request = new ItemRequestDto();
        request.setName("Phone");
        request.setPrice(BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}