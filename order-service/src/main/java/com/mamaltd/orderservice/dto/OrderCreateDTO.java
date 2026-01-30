package com.mamaltd.orderservice.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
public class OrderCreateDTO {
    @NotNull
    private Long userId;
    @NotNull
    private List<OrderItemDTO> items = new ArrayList<>();

    @Data
    public static class OrderItemDTO {
        @NotNull
        private Long productId;
        
        @NotNull
        @Min(1)
        private Integer quantity;
    }
}
