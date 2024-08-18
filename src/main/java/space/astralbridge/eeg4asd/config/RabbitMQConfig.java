package space.astralbridge.eeg4asd.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class RabbitMQConfig {
    @Value("${spring.rabbitmq.queues.request}")
    private String requestQueueName;

    @Value("${spring.rabbitmq.queues.response}")
    private String responseQueueName;

    @Bean
    public Queue requestQueue() {
        return new Queue(requestQueueName);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(responseQueueName);
    }

}
