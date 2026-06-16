package by.innowise.course.dto.item;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemFilterDto {
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}