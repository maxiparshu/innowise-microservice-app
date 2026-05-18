package by.innowise.course.repository;

import by.innowise.course.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
        , JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.active = :active
            """)
    List<User> findAllByActive(Boolean active);

    @Query(
            value = """
                    SELECT *
                    FROM users
                    WHERE birth_date < CURRENT_DATE - INTERVAL '18 years'
                    """,
            nativeQuery = true
    )
    List<User> findAllAdultUsers();

    @Query(value = """
            SELECT *
            FROM users
            ORDER BY created_at DESC
            LIMIT 10
            """, nativeQuery = true)
    List<User> findLastCreatedUsers();
}
