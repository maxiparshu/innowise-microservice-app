package by.innowise.course.specification;

import by.innowise.course.entity.Order;
import by.innowise.course.entity.OrderStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderSpecificationTest {

    @Test
    void betweenDatesWhenDatesAreNull() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<Order> root = mock(Root.class);
        Predicate predicate = mock(Predicate.class);

        when(cb.conjunction()).thenReturn(predicate);

        Specification<Order> specification =
                OrderSpecification.betweenDates(null, null);

        Predicate result = specification.toPredicate(root, null, cb);

        assertNotNull(result);
        verify(cb).conjunction();
    }

    @Test
    void betweenDatesShouldUseLessThanOrEqualToWhenOnlyEndExists() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<Order> root = mock(Root.class);
        Path<LocalDateTime> createdAt = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        LocalDate end = LocalDate.of(2025, 1, 31);
        LocalDateTime expected = end.atTime(LocalTime.MAX);

        when(root.<LocalDateTime>get("createdAt")).thenReturn(createdAt);
        when(cb.lessThanOrEqualTo(createdAt, expected))
                .thenReturn(predicate);

        Specification<Order> specification =
                OrderSpecification.betweenDates(null, end);

        Predicate result = specification.toPredicate(root, null, cb);

        assertNotNull(result);

        verify(cb).lessThanOrEqualTo(createdAt, expected);
    }

    @Test
    void betweenDatesShouldUseGreaterThanOrEqualToWhenOnlyStartExists() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<Order> root = mock(Root.class);
        Path<LocalDateTime> createdAt = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDateTime expected = start.atStartOfDay();

        when(root.<LocalDateTime>get("createdAt")).thenReturn(createdAt);
        when(cb.greaterThanOrEqualTo(createdAt, expected))
                .thenReturn(predicate);

        Specification<Order> specification =
                OrderSpecification.betweenDates(start, null);

        Predicate result = specification.toPredicate(root, null, cb);

        assertNotNull(result);

        verify(cb).greaterThanOrEqualTo(createdAt, expected);
    }

    @Test
    void betweenDatesShouldUseBetweenWhenBothDatesExist() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<Order> root = mock(Root.class);
        Path<LocalDateTime> createdAt = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 1, 31);

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        when(root.<LocalDateTime>get("createdAt")).thenReturn(createdAt);
        when(cb.between(createdAt, startDateTime, endDateTime))
                .thenReturn(predicate);

        Specification<Order> specification =
                OrderSpecification.betweenDates(start, end);

        Predicate result = specification.toPredicate(root, null, cb);

        assertNotNull(result);

        verify(cb).between(
                createdAt,
                startDateTime,
                endDateTime
        );
    }

    @Test
    void whenStatusesAreNull() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<Order> root = mock(Root.class);
        Predicate predicate = mock(Predicate.class);

        when(cb.conjunction()).thenReturn(predicate);

        Specification<Order> specification =
                OrderSpecification.hasStatuses(null);

        Predicate result = specification.toPredicate(root, null, cb);

        assertNotNull(result);
        verify(cb).conjunction();
    }

    @Test
    void whenStatusesAreEmpty() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<Order> root = mock(Root.class);
        Predicate predicate = mock(Predicate.class);

        when(cb.conjunction()).thenReturn(predicate);

        Specification<Order> specification =
                OrderSpecification.hasStatuses(List.of());

        Predicate result = specification.toPredicate(root, null, cb);

        assertNotNull(result);
        verify(cb).conjunction();
    }

    @Test
    @SuppressWarnings("unchecked")
    void hasStatusesShouldUseInClause() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<Order> root = mock(Root.class);

        Path<OrderStatus> statusPath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        List<OrderStatus> statuses = List.of(
                OrderStatus.CREATED,
                OrderStatus.CANCELLED
        );

        when(root.<OrderStatus>get("status")).thenReturn(statusPath);
        when(statusPath.in(statuses)).thenReturn(predicate);

        Specification<Order> specification =
                OrderSpecification.hasStatuses(statuses);

        Predicate result = specification.toPredicate(root, null, cb);

        assertNotNull(result);

        verify(statusPath).in(statuses);
    }
}