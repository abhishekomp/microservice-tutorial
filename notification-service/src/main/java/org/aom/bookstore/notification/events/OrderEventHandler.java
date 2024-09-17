package org.aom.bookstore.notification.events;

import org.aom.bookstore.notification.domain.NotificationService;
import org.aom.bookstore.notification.domain.OrderEventEntity;
import org.aom.bookstore.notification.domain.OrderEventRepository;
import org.aom.bookstore.notification.domain.model.OrderCancelledEvent;
import org.aom.bookstore.notification.domain.model.OrderCreatedEvent;
import org.aom.bookstore.notification.domain.model.OrderDeliveredEvent;
import org.aom.bookstore.notification.domain.model.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author : abhishek
 * @since : Tue, 2024-Sep-17
 * Created with IntelliJ IDEA
 */
@Component
class OrderEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final NotificationService notificationService;

    private final OrderEventRepository orderEventRepository;

    OrderEventHandler(NotificationService notificationService, OrderEventRepository orderEventRepository) {
        this.notificationService = notificationService;
        this.orderEventRepository = orderEventRepository;
    }

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event){
        log.info("Read Order Created Event: {} ", event);
        //System.out.println("Read Order Created Event = " + event);

        if(orderEventRepository.existsByEventId(event.eventId())){
            log.warn("Received duplicate OrderCreatedEvent with eventId: {}", event.eventId());
            return;
        }
        //Once the event has been read, we need the implementation to send an email to the customer
        log.info("Received a OrderCreatedEvent with orderNumber:{}: ", event.orderNumber());
        notificationService.sendOrderCreatedNotification(event);
        var orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
        //notificationService.sendOrderCreatedNotification(event);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event){
        log.info("Read Order Delivered Event: {} ", event);
        //System.out.println("Read Order Delivered Event = " + event);
        if(orderEventRepository.existsByEventId(event.eventId())){
            log.warn("Received duplicate OrderDeliveredEvent with eventId: {}", event.eventId());
            return;
        }
        //Once the event has been read, we need the implementation to send an email to the customer
        log.info("Received a OrderDeliveredEvent with orderNumber:{}: ", event.orderNumber());
        notificationService.sendOrderDeliveredNotification(event);
        var orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
        //notificationService.sendOrderDeliveredNotification(event);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event){
        log.info("Read Order Cancelled Event: {} ", event);
        //System.out.println("Read Order Cancelled Event = " + event);
        if(orderEventRepository.existsByEventId(event.eventId())){
            log.warn("Received duplicate OrderCancelledEvent with eventId: {}", event.eventId());
            return;
        }

        //Once the event has been read, we need the implementation to send an email to the customer
        notificationService.sendOrderCancelledNotification(event);
        log.info("Received a OrderCancelledEvent with orderNumber:{}: ", event.orderNumber());
        var orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
        //notificationService.sendOrderCancelledNotification(event);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderErrorEvent(OrderErrorEvent event){
        log.info("Read Order Error Event: {} ", event);
        //System.out.println("Read Order Error Event = " + event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderErrorEvent with eventId: {}", event.eventId());
            return;
        }

        //Once the event has been read, we need the implementation to send an email to the backend team
        notificationService.sendOrderErrorEventNotification(event);
        log.info("Received a OrderErrorEvent with orderNumber:{}: ", event.orderNumber());
        notificationService.sendOrderErrorEventNotification(event);
        OrderEventEntity orderEvent = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEvent);
    }
}
