package com.mamaltd.productservice.dto;

import lombok.Data;

@Data
public class StockUpdateDTO {

    private Integer delta;
    private Integer newStock;
}