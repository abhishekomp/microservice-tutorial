package org.aom.bookstore.orders.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aom.bookstore.orders.ApplicationProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RabbitMQConfig {
    private final ApplicationProperties properties;

    public RabbitMQConfig(ApplicationProperties applicationProperties) {
        this.properties = applicationProperties;
    }

//    @Bean
//    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
//        return new RabbitAdmin(connectionFactory);
//    }

    @Bean
    DirectExchange exchange(){
       return new DirectExchange(properties.orderEventsExchange());
    }

    @Bean
    Queue newOrdersQueue(){
        String newOrdersQueue = properties.newOrdersQueue();
        //System.out.println("newOrdersQueue = " + newOrdersQueue);
        return QueueBuilder.durable(properties.newOrdersQueue()).build();
    }

    @Bean
    Binding newOrdersQueueBinding(){
        return BindingBuilder.bind(newOrdersQueue()).to(exchange()).with(properties.newOrdersQueue());
    }

    @Bean
    Queue deliveredOrdersQueue(){
        return QueueBuilder.durable(properties.deliveredOrdersQueue()).build();
    }

    @Bean
    Binding deliveredOrdersQueueBinding(){
        return BindingBuilder.bind(deliveredOrdersQueue()).to(exchange()).with(properties.deliveredOrdersQueue());
    }

    @Bean
    Queue cancelledOrdersQueue(){
        return QueueBuilder.durable(properties.cancelledOrdersQueue()).build();
    }

    @Bean
    Binding cancelledOrdersQueueBinding(){
        return BindingBuilder.bind(cancelledOrdersQueue()).to(exchange()).with(properties.cancelledOrdersQueue());
    }

    @Bean
    Queue errorOrdersQueue() {
        return QueueBuilder.durable(properties.errorOrdersQueue()).build();
    }

    @Bean
    Binding errorOrdersQueueBinding() {
        return BindingBuilder.bind(errorOrdersQueue()).to(exchange()).with(properties.errorOrdersQueue());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter(objectMapper));
       return rabbitTemplate;
    }

    @Bean
    public MessageConverter jacksonConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }
}
