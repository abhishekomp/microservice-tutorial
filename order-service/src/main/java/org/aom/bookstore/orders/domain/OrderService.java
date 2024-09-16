package org.aom.bookstore.orders.domain;

import org.aom.bookstore.orders.domain.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("INDIA", "USA", "GERMANY", "UK");

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator, OrderEventService orderEventService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderEventService = orderEventService;
    }

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request) {
        log.info("OrderService::createOrder() was called");
        orderValidator.validate(request);
        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        newOrder.setUserName(userName);
        OrderEntity savedOrder = this.orderRepository.save(newOrder);
        log.info("Created Order with orderNumber={}", savedOrder.getOrderNumber());
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(savedOrder);
        orderEventService.save(orderCreatedEvent);

        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }

    public void processNewOrders() {
        log.info("OrderService::processNewOrders() was called");
        List<OrderEntity> orders = orderRepository.findByStatus(OrderStatus.NEW);
        log.info("Found {} new orders to process", orders.size());
        for(OrderEntity order: orders){
            this.process(order);
        }
    }

    private void process(OrderEntity order) {
        try{
            log.info("OrderService::process() was called for order number: {}", order.getOrderNumber());
            if(canBeDelivered(order)){
                //update the status of the Order as Delivered and create and save an event in Order Events table
                //order.setStatus(OrderStatus.DELIVERED);
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                log.info("Order number: {} was updated as DELIVERED", order.getOrderNumber());

                //Create an instance of OrderDelivered Event and save it in OrderEvents table.
                orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(order));
            } else {
                log.info("OrderNumber: {} can not be delivered due to location {}", order.getOrderNumber(), order.getDeliveryAddress().country());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventService.save(
                        OrderEventMapper.buildOrderCancelledEvent(order, "Can't deliver to the location"));
            }
        } catch (RuntimeException e) {
            log.error("Failed to process Order with orderNumber: {}. Exception: {}", order.getOrderNumber(), e);
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(OrderEventMapper.buildOrderErrorEvent(order, e.getMessage()));
        }
    }

    private boolean canBeDelivered(OrderEntity order) {
        return DELIVERY_ALLOWED_COUNTRIES.contains(order.getDeliveryAddress().country().toUpperCase());
    }

    public List<OrderSummary> findOrders(String userName) {
        List<OrderSummary> list = orderRepository.findByUserName(userName);
        log.info("Fetched {} orders for user: {}", list.size(), userName);
        return list;
    }

    public Optional<OrderDTO> findUserOrder(String userName, String orderNumber) {
        return orderRepository.findByUserNameAndOrderNumber(userName, orderNumber)
                .map(OrderMapper::convertToDTO);
    }
}
