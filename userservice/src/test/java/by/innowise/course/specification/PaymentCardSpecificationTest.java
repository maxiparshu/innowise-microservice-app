package by.innowise.course.specification;

import by.innowise.course.entity.PaymentCard;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
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


class PaymentCardSpecificationTest {

    @Test
    void hasUserNameShouldReturnNullWhenNameIsNull() {

        Specification<PaymentCard> specification =
                PaymentCardSpecification.hasUserName(null);

        Root<PaymentCard> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        assertNull(specification.toPredicate(root, null, cb));
    }

    @Test
    void hasUserNameShouldReturnNullWhenNameIsBlank() {

        Specification<PaymentCard> specification =
                PaymentCardSpecification.hasUserName(" ");

        Root<PaymentCard> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        assertNull(specification.toPredicate(root, null, cb));
    }

    @Test
    void hasUserNameShouldCreatePredicate() {

        Root<PaymentCard> root = mock(Root.class);
        Join<Object, Object> join = mock(Join.class);
        Path<Object> path = mock(Path.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate predicate = mock(Predicate.class);

        when(root.join("user")).thenReturn(join);
        when(join.get("name")).thenReturn(path);
        when(cb.equal(path, "Ivan")).thenReturn(predicate);

        Predicate result = PaymentCardSpecification.hasUserName("Ivan")
                .toPredicate(root, null, cb);

        assertNotNull(result);
        assertEquals(predicate, result);

        verify(root).join("user");
        verify(join).get("name");
        verify(cb).equal(path, "Ivan");
    }

    @Test
    void hasUserSurnameShouldReturnNullWhenSurnameIsNull() {

        Specification<PaymentCard> specification =
                PaymentCardSpecification.hasUserSurname(null);

        Root<PaymentCard> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        assertNull(specification.toPredicate(root, null, cb));
    }

    @Test
    void hasUserSurnameShouldReturnNullWhenSurnameIsBlank() {

        Specification<PaymentCard> specification =
                PaymentCardSpecification.hasUserSurname(" ");

        Root<PaymentCard> root = mock(Root.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        assertNull(specification.toPredicate(root, null, cb));
    }

    @Test
    void hasUserSurnameShouldCreatePredicate() {

        Root<PaymentCard> root = mock(Root.class);
        Join<Object, Object> join = mock(Join.class);
        Path<Object> path = mock(Path.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate predicate = mock(Predicate.class);

        when(root.join("user")).thenReturn(join);
        when(join.get("surname")).thenReturn(path);
        when(cb.equal(path, "Ivanov")).thenReturn(predicate);

        Predicate result = PaymentCardSpecification.hasUserSurname("Ivanov")
                .toPredicate(root, null, cb);

        assertNotNull(result);
        assertEquals(predicate, result);

        verify(root).join("user");
        verify(join).get("surname");
        verify(cb).equal(path, "Ivanov");
    }
}