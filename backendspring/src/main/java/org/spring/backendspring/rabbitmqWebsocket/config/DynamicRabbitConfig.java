package org.spring.backendspring.rabbitmqWebsocket.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamicRabbitConfig {
    
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    // @Bean
    // public TopicExchange topicExchange() {
    //     return new TopicExchange(exchangeName);
    // }

    public Queue createCrewQueue(String crewId) {
        return new Queue("chat.queue.crew." + crewId, true);
    }

    public Binding bindCrewQueue(String crewId, TopicExchange exchange) {
        Queue queue = createCrewQueue(crewId);
        return BindingBuilder.bind(queue).to(exchange).with("chat.queue.crew" + crewId);
    }
}
