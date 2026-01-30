package com.mamaltd.orderservice.dto;

//import com.mamaltd.orderservice.entity.Order;
import com.mamaltd.orderservice.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderUpdateDTO {
    private OrderStatus status; // NEW, PROCESSING, COMPLETED, CANCELLED
}
