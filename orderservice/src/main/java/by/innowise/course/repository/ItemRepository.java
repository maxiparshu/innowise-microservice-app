package by.innowise.course.repository;

import by.innowise.course.entity.Item;
import by.innowise.course.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>,
        JpaSpecificationExecutor<Item> {
}
