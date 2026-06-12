package by.innowise.course.specification;

import by.innowise.course.entity.PaymentCard;
import org.springframework.data.jpa.domain.Specification;

public class PaymentCardSpecification {
    private PaymentCardSpecification() {

    }

    public static Specification<PaymentCard> hasUserName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();

            }

            return criteriaBuilder.equal(
                    root.join("user").get("name"),
                    name
            );
        };
    }

    public static Specification<PaymentCard> hasUserSurname(String surname) {
        return (root, query, criteriaBuilder) -> {
            if (surname == null || surname.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.join("user").get("surname"),
                    surname
            );
        };
    }
}
