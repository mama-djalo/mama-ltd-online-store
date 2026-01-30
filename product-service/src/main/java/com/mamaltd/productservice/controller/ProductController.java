package com.mamaltd.productservice.controller;

import com.mamaltd.productservice.dto.ProductCreateDTO;
import com.mamaltd.productservice.dto.ProductResponseDTO;
import com.mamaltd.productservice.dto.ProductUpdateDTO;
import com.mamaltd.productservice.dto.StockUpdateDTO;
import com.mamaltd.productservice.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products") // Base path for all product APIs
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id) {
        return productService.getProductResponseById(id);
        }

    // Create new product
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
        @Valid @RequestBody ProductCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(dto));
    }

    // Update existing product
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
        @PathVariable Long id, 
        @RequestBody ProductUpdateDTO dto) {
            return ResponseEntity.ok(productService.updateProduct(id, dto));
       
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
    }



    // Update stock only
    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDTO> updateProductStock(
        @PathVariable Long id, 
        @RequestBody StockUpdateDTO dto) {
        return ResponseEntity.ok(productService.updateProductStock(id, dto));
    }     
}
