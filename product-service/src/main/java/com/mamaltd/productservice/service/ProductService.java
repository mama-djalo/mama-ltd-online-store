package com.mamaltd.productservice.service;

import com.mamaltd.productservice.dto.ProductCreateDTO;
import com.mamaltd.productservice.dto.ProductResponseDTO;
import com.mamaltd.productservice.dto.ProductUpdateDTO;
import com.mamaltd.productservice.dto.StockUpdateDTO;
import com.mamaltd.productservice.entity.Product;
import com.mamaltd.productservice.exception.ProductNotFoundException;
import com.mamaltd.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // Create a new product
    public ProductResponseDTO createProduct(ProductCreateDTO dto) {
        Product product = mapper.toEntity(dto);
        return mapper.toResponseDTO(repository.save(product));
    }

    // Get a product by ID
    public ProductResponseDTO getProductResponseById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return mapper.toResponseDTO(product);
    }

    // Get all products
    public List<ProductResponseDTO> getAllProducts() {
        return repository.findAll()
                 .stream()
                 .map(mapper::toResponseDTO)
                 .toList();
    }

    // Update a product
public ProductResponseDTO updateProduct(Long id, ProductUpdateDTO dto) {
    Product product = repository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
    mapper.updateEntity(dto, product);
    return mapper.toResponseDTO(product);        
}

    // Update stock (for orders)
    @Transactional
    public ProductResponseDTO updateProductStock(Long id, StockUpdateDTO dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (dto.getNewStock() != null) {
            if (dto.getNewStock() < 0) {
                throw new IllegalArgumentException("Stock quantity cannot be negative");
            }
            product.setStockQuantity(dto.getNewStock());
        } else if (dto.getDelta() != null) {
            int newStock = product.getStockQuantity() + dto.getDelta();
            if (newStock < 0) {
                throw new IllegalArgumentException("Insufficient stock for the operation");
            }
            product.setStockQuantity(newStock);
        } else {
            throw new IllegalArgumentException("Either delta or newStock must be provided");    
        } 
        // Save changes      
        return mapper.toResponseDTO(repository.save(product));
    }

    // Delete a product
    public void deleteProduct(Long id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
