package com.mamaltd.orderservice.service;

import com.mamaltd.orderservice.client.ProductClient;
import com.mamaltd.orderservice.dto.OrderCreateDTO;
import com.mamaltd.orderservice.dto.ProductResponseDTO;
import com.mamaltd.orderservice.dto.StockUpdateDTO;
import com.mamaltd.orderservice.entity.Order;
import com.mamaltd.orderservice.entity.OrderItem;
import com.mamaltd.orderservice.enums.OrderStatus;
import com.mamaltd.orderservice.exception.InsufficientStockException;
import com.mamaltd.orderservice.exception.OrderNotFoundException;
import com.mamaltd.orderservice.exception.ProductNotFoundException;
import com.mamaltd.orderservice.repository.OrderRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService  {

    // Implement business logik for order
    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    public OrderService(ProductClient productClient, OrderRepository orderRepository) {
        this.productClient = productClient;
        this.orderRepository = orderRepository;
    }

    public ProductResponseDTO getProductDetails(Long productId) {
        return productClient.getProductById(productId);
    }

/* Create a new order with automatic:
  - Product validation
  - Stock checking
  - Price calculation
  - Stock reduction
 */

@Transactional
public Order createOrder(OrderCreateDTO dto) {
    List<OrderItem> orderItems = new ArrayList<>();
    BigDecimal totalPrice = BigDecimal.ZERO;

    // Process each item in the order
    for (OrderCreateDTO.OrderItemDTO itemDTO : dto.getItems()) {
        ProductResponseDTO product;
        try {
            product = productClient.getProductById(itemDTO.getProductId());
        } catch (FeignException.NotFound ex) {
            throw new ProductNotFoundException(itemDTO.getProductId());
        }

        // Check stock availability
        if (product.getStockQuantity() < itemDTO.getQuantity()) {
            throw new InsufficientStockException(
                product.getName(),
                product.getStockQuantity(),
                itemDTO.getQuantity()
                );
        }

        // Create order item
        OrderItem orderItem = OrderItem.builder()
                .productId(product.getProductId())
                .quantity(itemDTO.getQuantity())
                .unitPrice(product.getPrice())
                .build();

        orderItems.add(orderItem);
        
        // Calculate item total and add to total price 
        BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
        totalPrice = totalPrice.add(itemTotal);

        // Update stock in Product Service 
        StockUpdateDTO stockDto = new StockUpdateDTO();
        stockDto.setDelta(-itemDTO.getQuantity());
        productClient.updateProductStock(product.getProductId(), stockDto);
    }

    // Create the order
    Order order = Order.builder()
            .userId(dto.getUserId())
            .orderDate(LocalDateTime.now())
            .totalPrice(totalPrice)
            .status(OrderStatus.NEW)
            .items(orderItems)
            .build();

    // Bidirectional relationship setup
    for (OrderItem item : orderItems) {
        item.setOrder(order);
    }
    order.setItems(orderItems);
    return orderRepository.save(order);
} 
    // Get order by ID
public Order getOrderById(Long orderId) {
    return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException (orderId ));
}
    // Get all orders
public List<Order> getAllOrders() {
    return orderRepository.findAll();
}

// Update order status
@Transactional
public Order updateOrderStatus(Long orderId, OrderStatus status) {
    Order order = getOrderById(orderId);
    order.setStatus(status);
    return orderRepository.save(order);
}

// cancel an order and restore stock
@Transactional
public Order cancelOrder(Long orderId) {
    Order order = getOrderById(orderId);
    
    if (order.getStatus() == OrderStatus.CANCELLED) {
        throw new IllegalStateException("Order is already cancelled.");
    }

    if (order.getStatus() == OrderStatus.COMPLETED) {
        throw new IllegalStateException("Completed orders cannot be cancelled.");
    }

    // Restore stock for all item
    for (OrderItem item : order.getItems()) {
        ProductResponseDTO product = productClient.getProductById(item.getProductId());
        int restoredStock = product.getStockQuantity() + item.getQuantity();
       
        StockUpdateDTO stockDto = new StockUpdateDTO();
        stockDto.setDelta(item.getQuantity());
        productClient.updateProductStock(item.getProductId(), stockDto);
    }

    order.setStatus(OrderStatus.CANCELLED);
    return orderRepository.save(order);
  }

// Delete an order
@Transactional
public void deleteOrder(Long orderId) {
    Order order = getOrderById(orderId);

    // Restore stock before deleting
    for (OrderItem item : order.getItems()) {
        ProductResponseDTO product = productClient.getProductById(item.getProductId());
        StockUpdateDTO stockDto = new StockUpdateDTO();
        stockDto.setNewStock(product.getStockQuantity() + item.getQuantity());
        productClient.updateProductStock(item.getProductId(), stockDto);
    }
    orderRepository.deleteById(orderId);
 }
}


