package by.innowise.course.controller;

import by.innowise.course.dto.LoginRequest;
import by.innowise.course.dto.RefreshTokenRequest;
import by.innowise.course.dto.RegisterRequest;
import by.innowise.course.entity.RefreshToken;
import by.innowise.course.entity.Role;
import by.innowise.course.entity.UserCredential;
import by.innowise.course.repository.RefreshTokenRepository;
import by.innowise.course.repository.UserCredentialRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

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

        registry.add("jwt.access-token-expiration",
                () -> "3600000");

        registry.add("jwt.admin-password",
                () -> "12312314");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        refreshTokenRepository.deleteAll();
        userCredentialRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setUserId(-1L);
        request.setLogin("john");
        request.setPassword("1q2w3e4r5T");
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        request.setUserId(1L);


        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {

        UserCredential user = new UserCredential();
        user.setUserId(1L);
        user.setLogin("john");
        user.setPasswordHash(passwordEncoder.encode("1q2w3e4r5T"));
        user.setRole(Role.USER);
        user.setActive(true);

        userCredentialRepository.save(user);

        LoginRequest request = new LoginRequest();
        request.setLogin("john");
        request.setPassword("password");
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
             .andExpect(status().isBadRequest());
        request.setPassword("11113e4r5T");
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        request.setPassword("1q2w3e4r5T");
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void shouldRefreshTokenSuccessfully() throws Exception {

        UserCredential user = new UserCredential();
        user.setUserId(1L);
        user.setLogin("john");
        user.setPasswordHash(passwordEncoder.encode("1q2w3e4r5T"));
        user.setRole(Role.USER);
        user.setActive(true);

        userCredentialRepository.save(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(1L);
        refreshToken.setToken("refresh-token");
        refreshToken.setRevoked(true);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(1));

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-token");

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        refreshTokenRepository.save(refreshToken);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        refreshToken.setRevoked(false);

        refreshTokenRepository.saveAndFlush(refreshToken);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void shouldValidateTokenSuccessfully() throws Exception {

        UserCredential user = new UserCredential();
        user.setUserId(1L);
        user.setLogin("john");
        user.setPasswordHash(passwordEncoder.encode("1q2w3e4r5T"));
        user.setRole(Role.USER);
        user.setActive(true);

        userCredentialRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("1q2w3e4r5T");
        loginRequest.setLogin("john");

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(response)
                .get("accessToken")
                .asText();

        mockMvc.perform(get("/auth/validate")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void shouldLogoutSuccessfully() throws Exception {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(1L);
        refreshToken.setToken("refresh-token");
        refreshToken.setRevoked(false);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(1));

        refreshTokenRepository.save(refreshToken);

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-token");

        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
}