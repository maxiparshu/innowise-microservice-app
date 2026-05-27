package by.innowise.course.repository;

import by.innowise.course.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
        , JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
    @EntityGraph(attributePaths = "paymentCards")
    Optional<User> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.id = :id")
    Optional<User> findByIdForUpdate(Long id);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.active = true
            """)
    Page<User> findActiveUsers(Pageable pageable);

    @Query(
            value = """
                SELECT  u.id, u.name, u.surname, u.birth_date, u.email, u.active, u.created_at, u.updated_at
                FROM users u
                LEFT JOIN payment_cards pc
                    ON u.id = pc.user_id
                WHERE pc.id IS NULL
                """,
            countQuery = "SELECT count(*) FROM users u LEFT JOIN payment_cards pc ON u.id = pc.user_id WHERE pc.id IS NULL",
            nativeQuery = true
    )
    Page<User> findUsersWithoutCards(Pageable pageable);
}
