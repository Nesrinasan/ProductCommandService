package com.backendguru.productService.dto;

public record ProductCreatedEvent(String productId, String name, String category, Double price) {
}
