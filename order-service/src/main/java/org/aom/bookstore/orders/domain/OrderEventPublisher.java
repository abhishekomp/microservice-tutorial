package org.aom.bookstore.orders.domain;

import org.aom.bookstore.orders.ApplicationProperties;
import org.aom.bookstore.orders.domain.model.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;

    OrderEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    void publish(OrderCreatedEvent orderCreatedEvent) {
        this.send(properties.newOrdersQueue(), orderCreatedEvent);
    }

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, payload);
    }
}
