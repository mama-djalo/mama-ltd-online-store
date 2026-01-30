package com.mamaltd.productservice.service;

import com.mamaltd.productservice.dto.ProductCreateDTO;
import com.mamaltd.productservice.dto.ProductResponseDTO;
import com.mamaltd.productservice.dto.ProductUpdateDTO;
import com.mamaltd.productservice.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    // Create
    public Product toEntity(ProductCreateDTO dto) {
        
        return Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .category(dto.getCategory())
                .build();
    }

    // Update
public void updateEntity(ProductUpdateDTO dto, Product product) {
    if (dto.getName() != null) {
        product.setName(dto.getName());
    }
    if (dto.getDescription() != null) {
    product.setDescription(dto.getDescription());
    }
    if (dto.getPrice() != null) {
        product.setPrice(dto.getPrice());
    }
    if (dto.getStockQuantity() != null) {
        product.setStockQuantity(dto.getStockQuantity());
    }
    if (dto.getCategory() != null) {
        product.setCategory(dto.getCategory());
    }
}

    // Response
    public ProductResponseDTO toResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategory(product.getCategory());
        dto.setVersion(product.getVersion());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}
