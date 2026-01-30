package com.mamaltd.orderservice.controller;

import com.mamaltd.orderservice.client.ProductClient;
import com.mamaltd.orderservice.dto.OrderCreateDTO;
import com.mamaltd.orderservice.dto.OrderUpdateDTO;
import com.mamaltd.orderservice.dto.ProductResponseDTO;
import com.mamaltd.orderservice.entity.Order;
import com.mamaltd.orderservice.service.OrderService;

import jakarta.validation.Valid;

//import org.apache.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductClient productClient;

    // Constructor
    public OrderController(OrderService orderService, ProductClient productClient) {
        this.orderService = orderService;
        this.productClient = productClient;
    }

    // ------------- Order Endpoints ------------
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    /* This create order with automatic:
      - Product validation
      - Stock checking
      - Price calculation
      - Stock reduction
     */
    @PostMapping
    public ResponseEntity <Order> createOrder(@Valid @RequestBody OrderCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(dto));
    }

    // Update order status
    @PatchMapping("/{id}/status")
    public Order updateOrderStatus(@PathVariable Long id, @Valid @RequestBody OrderUpdateDTO dto) {
        return orderService.updateOrderStatus(id, dto.getStatus());
          }

// Cancel order and restore stock
    @PostMapping("/{id}/cancel")
    public Order cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

// Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

//  ------------ Feign test endpoint ------------
    @GetMapping("/test/products")
    public List<ProductResponseDTO> getAllProductsFromProductService() {
        return productClient.getAllProducts();
    }

    @GetMapping("/test/products/{productId}")
    public ProductResponseDTO getProductByIdFromProductService(@PathVariable Long productId) {
        return productClient.getProductById(productId);
    }
}
