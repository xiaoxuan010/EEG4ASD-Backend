package space.astralbridge.eeg4asd.service.mq;

import java.time.Instant;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.astralbridge.eeg4asd.dto.request.llm.PostChatRequestDTO;
import space.astralbridge.eeg4asd.dto.response.llm.PostChatResponseDTO;
import space.astralbridge.eeg4asd.model.Task;
import space.astralbridge.eeg4asd.repository.TaskRepository;
import space.astralbridge.eeg4asd.service.bussine.LLMService;

@Component
@AllArgsConstructor
@Slf4j
public class MessageReceiveService {
    private final TaskRepository taskRepository;
    private final LLMService llmService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "#{rabbitMQConfig.getResponseQueueName()}")
    public void receiveResponse(String msgStr)
            throws JsonProcessingException, ApiException, NoApiKeyException, InputRequiredException {
        MessageResponse res = objectMapper.readValue(msgStr, MessageResponse.class);

        log.info("Received MQ Response for Task: " + res.getId());

        Task task = taskRepository.findById(res.getId()).orElseThrow();
        task.setResult(res.getResult());
        task.setState("done");
        task.setUpdatedAt(Instant.now());

        taskRepository.save(task);

        // 调用大模型获取分析情况
        String prompt = "先总结一下我的自闭症评级，再帮我总结这次的情绪分析结果，字数限制在150词以内，请使用纯文本回答。";
        PostChatRequestDTO requestDTO = new PostChatRequestDTO();
        requestDTO.setMessage(prompt);
        requestDTO.setTaskId(task.get_id());
        PostChatResponseDTO chatResponse = llmService.getChatResponse(requestDTO);
        task.setLlmResult(chatResponse.getMessage());

        taskRepository.save(task);

    }
}
