package org.aom.bookstore.notification.events;

import org.aom.bookstore.notification.domain.model.OrderCreatedEvent;
import org.aom.bookstore.notification.domain.model.OrderDeliveredEvent;
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

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event){
        log.info("Read Order Created Event: {} ", event);
        //System.out.println("Read Order Created Event = " + event);

        //Once the event has been read, we need the implementation to send an email to the customer
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event){
        log.info("Read Order Delivered Event: {} ", event);
        //System.out.println("Read Order Created Event = " + event);

        //Once the event has been read, we need the implementation to send an email to the customer
    }
}
