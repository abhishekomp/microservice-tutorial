package org.aom.bookstore.orders.clients.catalog;

import java.math.BigDecimal;

//public record Product(String code, String name, String description, String imageUrl, BigDecimal price) {}
public record Product(String code, String name, BigDecimal price) {}