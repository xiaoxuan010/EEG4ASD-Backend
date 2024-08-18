package space.astralbridge.eeg4asd.service.mq;

import java.time.Instant;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.astralbridge.eeg4asd.config.RabbitMQConfig;
import space.astralbridge.eeg4asd.model.Task;
import space.astralbridge.eeg4asd.repository.TaskRepository;
import space.astralbridge.eeg4asd.service.dto.MessageRequest;

@Component
@AllArgsConstructor
@Slf4j
public class MessageService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final TaskRepository taskRepository;
    private final RabbitMQConfig rabbitMQConfig;

    {
        log.info("MessageService Initialized");
        
    }

    public void sendRequest(MessageRequest req) throws JsonProcessingException {
        String msgStr = objectMapper.writeValueAsString(req);
        rabbitTemplate.convertAndSend(rabbitMQConfig.getRequestQueueName(), msgStr);
    }

    @RabbitListener(queues = "#{rabbitMQConfig.getResponseQueueName()}")
    public void receiveResponse(String msgStr) throws JsonProcessingException {
        MessageResponse res = objectMapper.readValue(msgStr, MessageResponse.class);

        log.info("Received MQ Response for Task: " + res.getId());

        Task task = taskRepository.findById(res.getId()).orElseThrow();
        task.setResult(res.getResult());
        task.setState("done");
        task.setUpdatedAt(Instant.now());

        taskRepository.save(task);

    }

}
