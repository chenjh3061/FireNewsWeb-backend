package com.example.firenewsbackend.config;


import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Binding;
import java.util.Queue;

//@Configuration
//public class RabbitMQConfig {
//    @Bean
//    public Queue queue() {
//        return new Queue("myQueue", false);
//    }
//
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange("myExchange");
//    }
//
//    @Bean
//    public Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("myRoutingKey");
//    }
//}

