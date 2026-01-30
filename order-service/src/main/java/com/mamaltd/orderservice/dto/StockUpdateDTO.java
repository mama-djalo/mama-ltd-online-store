package com.mamaltd.orderservice.dto;

import lombok.Data;
//import java.math.BigDecimal;

@Data
public class StockUpdateDTO {
    // Either delta or newStock should be provided
    private Integer delta;     // e.g., -2 to decrement, +5 to increment
    private Integer newStock;  // e.g., set stock to 10
}
