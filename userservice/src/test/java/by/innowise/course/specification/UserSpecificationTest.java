package by.innowise.course.specification;

import by.innowise.course.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserSpecificationTest {

    @Test
    void hasNameShouldReturnNullWhenNameIsNull() {

        Specification<User> specification =
                UserSpecification.hasName(null);

        Root<User> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        assertNull(specification.toPredicate(root, null, cb));
    }

    @Test
    void hasNameShouldReturnNullWhenNameIsBlank() {

        Specification<User> specification =
                UserSpecification.hasName(" ");

        Root<User> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        assertNull(specification.toPredicate(root, null, cb));
    }

    @Test
    void hasNameShouldCreateLikePredicate() {

        Root<User> root = mock(Root.class);
        Path<String> path = mock(Path.class);
        Expression<String> lowerExpression = mock(Expression.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate predicate = mock(Predicate.class);

        when(root.<String>get("name")).thenReturn(path);
        when(cb.lower(path)).thenReturn(lowerExpression);
        when(cb.like(lowerExpression, "%ivan%")).thenReturn(predicate);

        Predicate result = UserSpecification.hasName(" Ivan ")
                .toPredicate(root, null, cb);

        assertNotNull(result);
        assertEquals(predicate, result);

        verify(root).get("name");
        verify(cb).lower(path);
        verify(cb).like(lowerExpression, "%ivan%");
    }

    @Test
    void hasSurnameShouldReturnNullWhenSurnameIsNull() {

        Specification<User> specification =
                UserSpecification.hasSurname(null);

        Root<User> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        assertNull(specification.toPredicate(root, null, cb));
    }

    @Test
    void hasSurnameShouldReturnNullWhenSurnameIsBlank() {

        Specification<User> specification =
                UserSpecification.hasSurname(" ");

        Root<User> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        assertNull(specification.toPredicate(root, null, cb));
    }

    @Test
    void hasSurnameShouldCreateLikePredicate() {

        Root<User> root = mock(Root.class);
        Path<String> path = mock(Path.class);
        Expression<String> lowerExpression = mock(Expression.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate predicate = mock(Predicate.class);

        when(root.<String>get("surname")).thenReturn(path);
        when(cb.lower(path)).thenReturn(lowerExpression);
        when(cb.like(lowerExpression, "%ivanov%")).thenReturn(predicate);

        Predicate result = UserSpecification.hasSurname(" Ivanov ")
                .toPredicate(root, null, cb);

        assertNotNull(result);
        assertEquals(predicate, result);

        verify(root).get("surname");
        verify(cb).lower(path);
        verify(cb).like(lowerExpression, "%ivanov%");
    }
}