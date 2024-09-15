package org.aom.bookstore.orders.domain.model;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author : abhishek
 * @since : Sun, 2024-Sep-15
 * Created with IntelliJ IDEA
 */
public record OrderErrorEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        String reason,
        LocalDateTime createdAt) {}
