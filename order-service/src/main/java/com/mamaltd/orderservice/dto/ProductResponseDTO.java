package com.mamaltd.orderservice.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponseDTO {
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;
    private long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
}
