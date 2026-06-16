package by.innowise.course.specification;

import by.innowise.course.entity.Order;
import by.innowise.course.entity.OrderStatus;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class OrderSpecification {
    private OrderSpecification() {

    }

    public static Specification<Order> betweenDates(
            LocalDate start,
            LocalDate end
    ) {
        return (root, query, criteriaBuilder) -> {
            if (start == null && end == null) {
                return criteriaBuilder.conjunction();
            }

            Path<LocalDateTime> createdAt = root.get("createdAt");

            if (start == null) {
                return criteriaBuilder.lessThanOrEqualTo(createdAt, end.atTime(LocalTime.MAX));
            }

            if (end == null) {
                return criteriaBuilder.greaterThanOrEqualTo(createdAt, start.atStartOfDay());
            }

            return criteriaBuilder.between(createdAt, start.atStartOfDay(), end.atTime(LocalTime.MAX));
        };
    }

    public static Specification<Order> hasStatuses(List<OrderStatus> statuses) {
        return (root, query, criteriaBuilder) -> {
            if (statuses == null || statuses.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("status").in(statuses);
        };
    }
}