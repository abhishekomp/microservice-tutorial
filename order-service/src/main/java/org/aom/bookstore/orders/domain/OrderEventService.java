package org.aom.bookstore.orders.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aom.bookstore.orders.domain.model.OrderCreatedEvent;
import org.aom.bookstore.orders.domain.model.OrderEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderEventService {

    private static final Logger log = LoggerFactory.getLogger(OrderEventService.class);

    private final OrderEventRepository orderEventRepository;
    private final ObjectMapper objectMapper;

    OrderEventService(OrderEventRepository orderEventRepository, ObjectMapper objectMapper) {
        this.orderEventRepository = orderEventRepository;
        this.objectMapper = objectMapper;
    }

    void save(OrderCreatedEvent event) {
        log.info("OrderEventService::save() was invoked");
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CREATED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
        log.info("Created an event for OrderCreatedEvent with id={}", orderEvent.getId());
    }

    private String toJsonPayload(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
