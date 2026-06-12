package by.innowise.course.specification;

import by.innowise.course.entity.Item;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ItemSpecification {
    private ItemSpecification() {

    }

    public static Specification<Item> containName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.trim().toLowerCase() + "%"
            );
        };
    }

    public static Specification<Item> betweenPrice(BigDecimal lowEdge, BigDecimal topEdge) {
        return (root, query, criteriaBuilder) -> {
            Path<BigDecimal> price = root.get("price");

            if (lowEdge == null && topEdge == null) {
                return criteriaBuilder.conjunction();
            }
            if (lowEdge == null) {
                return criteriaBuilder.lessThanOrEqualTo(price, topEdge);
            }
            if (topEdge == null) {
                return criteriaBuilder.greaterThanOrEqualTo(price, lowEdge);
            }

            BigDecimal min = lowEdge.min(topEdge);
            BigDecimal max = lowEdge.max(topEdge);

            return criteriaBuilder.between(price, min, max);
        };
    }
}
