package by.innowise.course.config;


import by.innowise.course.entity.Role;
import by.innowise.course.entity.UserCredential;
import by.innowise.course.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializerConfig {


    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.admin-password}")
    private String password;

    @Bean
    public CommandLineRunner initAdmin(UserCredentialRepository repository) {
        return args -> {
            boolean exists = repository.findByLogin("admin").isPresent();
            if (exists) {
                return;
            }
            UserCredential admin = new UserCredential();
            admin.setUserId(0L);
            admin.setLogin("admin");
            admin.setPasswordHash(passwordEncoder.encode(password));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            repository.save(admin);
        };
    }
}
