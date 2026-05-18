package by.innowise.course.repository;

import by.innowise.course.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
        , JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.active = true
            """)
    Page<User> findActiveUsers(Pageable pageable);

    @Query(
            value = """
                    SELECT *
                    FROM users u
                    LEFT JOIN payment_cards pc
                        ON u.id = pc.user_id
                    WHERE pc.id IS NULL
                    """,
            nativeQuery = true
    )
    Page<User> findUsersWithoutCards(Pageable pageable);
}
