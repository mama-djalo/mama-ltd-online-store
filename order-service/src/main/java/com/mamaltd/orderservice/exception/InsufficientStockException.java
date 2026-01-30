package com.mamaltd.orderservice.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productName, int available, int requested) {
        super("Insufficient stock for product: " + productName 
            + ". Available: " + available 
            + ", Requested: " + requested);
    }
}