package com.mamaltd.productservice.dto;

import lombok.Data;
import java.math.BigDecimal;


@Data
public class ProductUpdateDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;
}
