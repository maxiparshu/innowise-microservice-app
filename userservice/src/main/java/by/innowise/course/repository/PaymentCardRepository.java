package by.innowise.course.repository;

import by.innowise.course.entity.PaymentCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {

    long countByUserId(Long userId);

    Page<PaymentCard> findByUserId(Long id, Pageable pageable);

    @Query("""
            SELECT pc
            FROM PaymentCard pc
            WHERE pc.active = true
            """)
    Page<PaymentCard> findActiveCards(Pageable pageable);

    @Query(
            value = """
                    SELECT *
                    FROM payment_cards
                    WHERE expiration_date <= CURRENT_DATE + INTERVAL '30 days'
                    """,
            nativeQuery = true
    )
    Page<PaymentCard> findCardsExpiringSoon(Pageable pageable);
}
