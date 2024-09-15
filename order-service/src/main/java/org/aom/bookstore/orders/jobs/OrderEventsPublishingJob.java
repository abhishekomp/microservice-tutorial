package org.aom.bookstore.orders.jobs;

import org.aom.bookstore.orders.domain.OrderEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
class OrderEventsPublishingJob {
    private static final Logger log = LoggerFactory.getLogger(OrderEventsPublishingJob.class);

    private final OrderEventService orderEventService;

    @Value("${order-service.publishEvents}")
    private boolean shouldPublishEvents;

    OrderEventsPublishingJob(OrderEventService orderEventService) {
        this.orderEventService = orderEventService;
    }

    @Scheduled(cron = "${orders.publish-order-events-job-cron}")
    //@SchedulerLock(name = "publishOrderEvents")
    public void publishOrderEvents() {
        //LockAssert.assertLocked();
        if(shouldPublishEvents){
            log.info("Publishing Order Events at {}", Instant.now());
            orderEventService.publishOrderEvents();
        } else {
            log.info("Skipping Publishing Order Events due to application property value set to: {}", shouldPublishEvents);
        }

    }
}