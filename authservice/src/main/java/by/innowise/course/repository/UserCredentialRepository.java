package by.innowise.course.repository;

import by.innowise.course.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

    Optional<UserCredential> findByLogin(String login);
    Optional<UserCredential> findByUserId(Long userId);
}
