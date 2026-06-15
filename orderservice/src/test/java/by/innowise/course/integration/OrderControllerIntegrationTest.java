package by.innowise.course.integration;


import by.innowise.course.dto.order.OrderRequestDto;
import by.innowise.course.dto.order.OrderUpdateRequestDto;
import by.innowise.course.dto.orderitem.OrderItemRequestDto;
import by.innowise.course.entity.Item;
import by.innowise.course.entity.Order;
import by.innowise.course.entity.OrderStatus;
import by.innowise.course.integration.config.WireMockServerConfig;
import by.innowise.course.repository.ItemRepository;
import by.innowise.course.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(WireMockServerConfig.class)
@ActiveProfiles("test")
class OrderControllerIntegrationTest {

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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();

        wireMockServer.resetAll();

        wireMockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo("/api/v1/users/1"))
                        .willReturn(
                                WireMock.okJson("""
                                        {
                                          "id": 1,
                                          "name": "John",
                                          "surname": "Doe",
                                          "email": "john@test.com"
                                        }
                                        """)
                        )
        );
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void createShouldSaveOrder() throws Exception {

        Item item = createItem();

        OrderItemRequestDto orderItem = new OrderItemRequestDto();
        orderItem.setItemId(item.getId());
        orderItem.setQuantity(2);

        OrderRequestDto request = new OrderRequestDto();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request.setUserId(1L);
        request.setStatus(OrderStatus.CREATED);
        request.setItems(List.of(orderItem));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1));

        assertEquals(1, orderRepository.count());

        Order order = orderRepository.findAll().getFirst();

        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(0,
                new BigDecimal("200.00")
                        .compareTo(order.getTotalPrice()));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void getByIdShouldReturnOrder() throws Exception {

        Item item = createItem();

        Order order = new Order();
        order.setUserId(1L);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalPrice(item.getPrice());

        order = orderRepository.save(order);

        mockMvc.perform(get("/api/v1/orders/{id}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.userId").value(1));

        mockMvc.perform(get("/api/v1/orders/-1"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/v1/orders/222"))
                .andExpect(status().isNotFound());

        order.setUserId(2L);
        order = orderRepository.save(order);

        mockMvc.perform(get("/api/v1/orders/{id}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.user.name").value("Unknown"))
                .andExpect(jsonPath("$.user.surname").value("Unknown"))
                .andExpect(jsonPath("$.user.email").value("Unknown"));

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getOrdersShouldReturnPage() throws Exception {

        Order order = new Order();
        order.setUserId(1L);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalPrice(BigDecimal.TEN);

        orderRepository.save(order);

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getOrdersByUserIdShouldReturnOrders() throws Exception {

        Order order = new Order();
        order.setUserId(1L);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalPrice(BigDecimal.TEN);

        orderRepository.save(order);

        mockMvc.perform(get("/api/v1/orders")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].userId").value(1));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateShouldUpdateOrder() throws Exception {

        Item item = createItem();

        Order order = new Order();
        order.setUserId(1L);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalPrice(BigDecimal.TEN);

        order = orderRepository.save(order);

        OrderItemRequestDto orderItem = new OrderItemRequestDto();
        orderItem.setItemId(item.getId());
        orderItem.setQuantity(3);

        OrderUpdateRequestDto request = new OrderUpdateRequestDto();
        request.setStatus(OrderStatus.SHIPPED);
        request.setItems(List.of(orderItem));

        mockMvc.perform(put("/api/v1/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("SHIPPED"));

        Order updated = orderRepository.findById(order.getId())
                .orElseThrow();

        assertEquals(OrderStatus.SHIPPED, updated.getStatus());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteShouldRemoveOrder() throws Exception {

        Order order = new Order();
        order.setUserId(1L);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalPrice(BigDecimal.TEN);

        order = orderRepository.save(order);

        mockMvc.perform(delete("/api/v1/orders/{id}", order.getId()))
                .andExpect(status().isNoContent());

        assertFalse(orderRepository.existsById(order.getId()));
    }

    @Test
    void createShouldReturn403WithoutAuthentication() throws Exception {

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    private Item createItem() {
        Item item = new Item();
        item.setName("Phone");
        item.setPrice(new BigDecimal("100.00"));
        return itemRepository.save(item);
    }
}