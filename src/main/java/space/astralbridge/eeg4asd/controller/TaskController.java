package space.astralbridge.eeg4asd.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import space.astralbridge.eeg4asd.dto.request.CreateTaskRequestDTO;
import space.astralbridge.eeg4asd.dto.request.GetTaskRequestDTO;
import space.astralbridge.eeg4asd.dto.request.PutTaskRequestDTO;
import space.astralbridge.eeg4asd.dto.response.CreateTaskResponseDTO;
import space.astralbridge.eeg4asd.dto.response.GetTaskResponseDTO;
import space.astralbridge.eeg4asd.dto.response.PutTaskResponseDTO;
import space.astralbridge.eeg4asd.service.bussine.TaskService;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public CreateTaskResponseDTO handleTaskPost(@Valid @ModelAttribute CreateTaskRequestDTO createTaskRequestDTO)
            throws IOException {
        return taskService.createTask(createTaskRequestDTO);
    }

    @GetMapping
    public GetTaskResponseDTO handleTaskGet(@Valid @ModelAttribute GetTaskRequestDTO getTaskRequestDTO) {
        return taskService.getTask(getTaskRequestDTO);
    }

    @PutMapping
    public PutTaskResponseDTO handleTaskPut(@Valid @RequestBody PutTaskRequestDTO requestDTO) {
        return taskService.putTask(requestDTO);
    }

    @DeleteMapping("/{taskId}")
    public void handleTaskDelete(@PathVariable("taskId") String taskId) {
        taskService.deleteTask(taskId);
    }

}
