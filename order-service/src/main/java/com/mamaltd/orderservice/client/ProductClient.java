package com.mamaltd.orderservice.client;

import com.mamaltd.orderservice.dto.ProductCreateDTO;
import com.mamaltd.orderservice.dto.ProductResponseDTO;
import com.mamaltd.orderservice.dto.StockUpdateDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
    name = "product-service", 
    url = "${product.service.url}",
    path = "/api/products"
)
public interface ProductClient {
    
    @GetMapping
    List<ProductResponseDTO> getAllProducts();

    @GetMapping("/{id}")
    ProductResponseDTO getProductById(@PathVariable("id")Long id);

    @PostMapping
    ProductResponseDTO createProduct(@RequestBody ProductCreateDTO productCreateDTO);

    @PutMapping("/{id}/stock")
    ProductResponseDTO updateProductStock(
        @PathVariable("id") Long id, 
        @RequestBody StockUpdateDTO dto
    );
}
