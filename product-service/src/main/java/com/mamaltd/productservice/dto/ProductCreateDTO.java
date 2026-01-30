package com.mamaltd.productservice.dto;

import lombok.Data;
import java.math.BigDecimal;

import jakarta.validation.constraints.*;


@Data
public class ProductCreateDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String description;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;
}
