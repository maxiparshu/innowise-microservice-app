package by.innowise.course.repository;

import by.innowise.course.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {

    List<PaymentCard> findAllByUserId(Long userId);

    long countByUserId(Long userId);
    @Query("""
       SELECT pc
       FROM PaymentCard pc
       WHERE pc.expirationDate < CURRENT_DATE
       """)
    List<PaymentCard> findExpiredCards();

    @Query(value = """
               SELECT *
               FROM payment_cards
               ORDER BY created_at DESC
               LIMIT 10
               """, nativeQuery = true)
    List<PaymentCard> findLastCreatedCards();
}
