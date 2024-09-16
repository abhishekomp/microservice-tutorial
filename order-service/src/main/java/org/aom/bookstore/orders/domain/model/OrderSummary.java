package org.aom.bookstore.orders.domain.model;

public record OrderSummary(String orderNumber, OrderStatus status) {}