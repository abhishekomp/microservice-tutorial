package org.aom.bookstore.orders.domain;

import org.aom.bookstore.orders.ApplicationProperties;
import org.aom.bookstore.orders.domain.model.OrderCancelledEvent;
import org.aom.bookstore.orders.domain.model.OrderCreatedEvent;
import org.aom.bookstore.orders.domain.model.OrderDeliveredEvent;
import org.aom.bookstore.orders.domain.model.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
class OrderEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;

    OrderEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    void publish(OrderCreatedEvent orderCreatedEvent) {
        this.send(properties.newOrdersQueue(), orderCreatedEvent);
    }

    void publish(OrderDeliveredEvent orderDeliveredEvent) {
        this.send(properties.deliveredOrdersQueue(), orderDeliveredEvent);
    }

    void publish(OrderCancelledEvent orderCancelledEvent) {
        this.send(properties.cancelledOrdersQueue(), orderCancelledEvent);
    }

    void publish(OrderErrorEvent orderErrorEvent) {
        this.send(properties.errorOrdersQueue(), orderErrorEvent);
    }

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, payload);
        log.info("Event published with routing key {}", routingKey);
        log.trace("Event '{}' published with routing key {}", payload, routingKey);
    }
}
