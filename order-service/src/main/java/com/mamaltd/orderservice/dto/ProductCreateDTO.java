package com.mamaltd.orderservice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductCreateDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;
    
}
