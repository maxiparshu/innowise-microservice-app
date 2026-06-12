package by.innowise.course.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Table(
        name = "items",
        indexes = {
                @Index(name = "idx_items_name", columnList = "name")
        }
)
@SQLDelete(sql = "UPDATE items SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@Getter
@Setter
public class Item extends BaseManageableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Column(nullable = false)
    private BigDecimal price;
}