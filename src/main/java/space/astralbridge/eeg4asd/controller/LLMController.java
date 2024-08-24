package space.astralbridge.eeg4asd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import space.astralbridge.eeg4asd.dto.request.llm.PostChatRequestDTO;
import space.astralbridge.eeg4asd.dto.response.llm.PostChatResponseDTO;
import space.astralbridge.eeg4asd.service.bussine.LLMService;

@RestController
@RequestMapping("/llm")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LLMController {
    private final LLMService llmService;

    @PostMapping("/chat")
    public PostChatResponseDTO postChat(@Valid @RequestBody PostChatRequestDTO requestDTO, HttpServletRequest request)
            throws ApiException, NoApiKeyException, InputRequiredException, JsonMappingException,
            JsonProcessingException {
        return llmService.getChatResponse(requestDTO);
    }

}
