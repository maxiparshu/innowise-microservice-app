package by.innowise.course.specification;


import by.innowise.course.entity.Item;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class ItemSpecificationTest {

    @Test
    void containNameShouldReturnNullWhenNameIsNull() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<Item> root = mock(Root.class);
        Predicate predicate = mock(Predicate.class);

        when(cb.conjunction()).thenReturn(predicate);

        Specification<Item> specification = ItemSpecification.betweenPrice(null, null);

        Predicate result = specification.toPredicate(root, null, cb);

        assertNotNull(result);
        verify(cb).conjunction();
    }

    @Test
    void containNameShouldReturnNullWhenNameIsBlank() {

        Specification<Item> specification =
                ItemSpecification.containName(" ");

        Root<Item> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        assertNull(specification.toPredicate(root, null, cb));
    }

    @Test
    void containNameNameShouldCreatePredicate() {

        Root<Item> root = mock(Root.class);
        Path<String> path = mock(Path.class);
        Expression<String> lowerExpression = mock(Expression.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate predicate = mock(Predicate.class);

        when(root.<String>get("name")).thenReturn(path);
        when(cb.lower(path)).thenReturn(lowerExpression);
        when(cb.like(lowerExpression, "%iphone%")).thenReturn(predicate);

        Predicate result = ItemSpecification.containName("iphone")
                .toPredicate(root, null, cb);

        assertNotNull(result);
        assertEquals(predicate, result);
    }


    @Test
    void betweenPriceShouldReturnConjunctionWhenBothEdgesAreNull() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<Item> root = mock(Root.class);
        Predicate predicate = mock(Predicate.class);

        when(cb.conjunction()).thenReturn(predicate);

        Specification<Item> specification = ItemSpecification.betweenPrice(null, null);

        Predicate result = specification.toPredicate(root, null, cb);

        assertNotNull(result);
        verify(cb).conjunction();
    }

    @Test
    void betweenPriceShouldUseLessThanOrEqualWhenOnlyTopEdgeExists() {
        Root<Item> root = mock(Root.class);
        Path<BigDecimal> pricePath = mock(Path.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate predicate = mock(Predicate.class);

        BigDecimal topEdge = BigDecimal.valueOf(100);
        when(root.<BigDecimal>get("price")).thenReturn(pricePath);
        when(cb.lessThanOrEqualTo(pricePath, topEdge)).thenReturn(predicate);

        Specification<Item> specification =
                ItemSpecification.betweenPrice(null, topEdge);

        Predicate result = specification.toPredicate(root, null, cb);

        assertNotNull(result);

        verify(cb).lessThanOrEqualTo(pricePath, topEdge);
    }

    @Test
    void betweenPriceShouldUseGreaterThanOrEqualWhenOnlyLowEdgeExists() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<Item> root = mock(Root.class);
        Path<BigDecimal> pricePath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        BigDecimal lowEdge = BigDecimal.valueOf(50);

        when(root.<BigDecimal>get("price")).thenReturn(pricePath);
        when(cb.greaterThanOrEqualTo(pricePath, lowEdge)).thenReturn(predicate);

        Specification<Item> specification =
                ItemSpecification.betweenPrice(lowEdge, null);

        Predicate result = specification.toPredicate(root, null, cb);

        assertNotNull(result);

        verify(cb).greaterThanOrEqualTo(pricePath, lowEdge);
    }

    @Test
    void betweenPriceShouldUseBetweenWhenBothEdgesExist() {
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Root<Item> root = mock(Root.class);
        Path<BigDecimal> pricePath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        BigDecimal lowEdge = BigDecimal.valueOf(100);
        BigDecimal topEdge = BigDecimal.valueOf(50);

        when(root.<BigDecimal>get("price")).thenReturn(pricePath);
        when(cb.between(
                pricePath,
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(100)))
                .thenReturn(predicate);

        Specification<Item> specification =
                ItemSpecification.betweenPrice(lowEdge, topEdge);

        Predicate result = specification.toPredicate(root, null, cb);

        assertNotNull(result);

        verify(cb).between(
                pricePath,
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(100)
        );
    }
}