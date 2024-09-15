package org.aom.bookstore.orders.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aom.bookstore.orders.domain.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderEventService {

    private static final Logger log = LoggerFactory.getLogger(OrderEventService.class);
    private final OrderEventRepository orderEventRepository;
    private final ObjectMapper objectMapper;
    private final OrderEventPublisher orderEventPublisher;

    OrderEventService(OrderEventRepository orderEventRepository, ObjectMapper objectMapper, OrderEventPublisher orderEventPublisher) {
        this.orderEventRepository = orderEventRepository;
        this.objectMapper = objectMapper;
        this.orderEventPublisher = orderEventPublisher;
    }

    void save(OrderCreatedEvent event) {
        log.info("OrderEventService::save() was invoked for OrderCreatedEvent");
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CREATED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
        log.info("Created an event for OrderCreatedEvent with id={}", orderEvent.getId());
    }

    void save(OrderDeliveredEvent event) {
        log.info("OrderEventService::save() was invoked for OrderDeliveredEvent");
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_DELIVERED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
        log.info("Created an event for OrderDeliveredEvent with id={}", orderEvent.getId());
    }

    void save(OrderCancelledEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CANCELLED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
        log.info("Created an event for OrderCancelledEvent with id={}", orderEvent.getId());
    }

    void save(OrderErrorEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_PROCESSING_FAILED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
        log.info("Created an event for OrderErrorEvent with id={}", orderEvent.getId());
    }

    private String toJsonPayload(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void publishOrderEvents() {
        Sort createdAt = Sort.by("createdAt").ascending();
        List<OrderEventEntity> events = orderEventRepository.findAll(createdAt);
        log.info("Found {} Order Events to be published", events.size());
        for(OrderEventEntity event : events){
            this.publish(event);
            orderEventRepository.delete(event);
        }
        log.info("Published {} events", events.size());
    }

    private void publish(OrderEventEntity event) {
        OrderEventType eventType = event.getEventType();
        switch (eventType){
            case ORDER_CREATED:
                OrderCreatedEvent orderCreatedEvent = fromJsonPayload(event.getPayload(), OrderCreatedEvent.class);
                orderEventPublisher.publish(orderCreatedEvent);
                break;
            default:
                log.warn("Unsupported OrderEventType: {}", eventType);
        }
    }

    private <T> T fromJsonPayload(String payload, Class<T> type) {
        try {
            return objectMapper.readValue(payload, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
