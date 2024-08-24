package space.astralbridge.eeg4asd.service.bussine;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import space.astralbridge.eeg4asd.dto.request.llm.PostChatRequestDTO;
import space.astralbridge.eeg4asd.dto.response.llm.PostChatResponseDTO;

@Service
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class LLMService {
        private final Generation generation;

        private final TaskService taskService;

        @Value("${llm.aliyun.apiKey}")
        @NotBlank(message = "apiKey is required")
        private String apiKey;

        @Value("${llm.prompt}")
        @NotBlank(message = "prompt is required")
        private String prompt;

        public PostChatResponseDTO getChatResponse(PostChatRequestDTO requestDTO)
                        throws ApiException, NoApiKeyException, InputRequiredException, JsonMappingException,
                        JsonProcessingException {
                String requestPrompt = prompt;

                if (requestDTO.getTaskId() != null) {
                        requestPrompt += "\n\n现在，我们的深度学习模型对患者的分析数据如下，你可以据此回答患者的问题。\n分析结果："
                                        + taskService.getTaskPredictionResultDescribtion(requestDTO.getTaskId());
                }

                log.info("requestPrompt: {}", requestPrompt);

                Message systemMsg = Message.builder().role(Role.SYSTEM.getValue())
                                .content(requestPrompt)
                                .build();

                Message userMsg = Message.builder().role(Role.USER.getValue()).content(requestDTO.getMessage()).build();

                GenerationParam param = GenerationParam.builder().model("qwen-turbo")
                                .messages(Arrays.asList(systemMsg, userMsg))
                                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                                .temperature(0.8f).build();

                log.info(param.toString());

                param.setApiKey(apiKey);

                GenerationResult result = generation.call(param);

                return new PostChatResponseDTO(result.getOutput().getChoices().get(0).getMessage().getContent());
        }
}
