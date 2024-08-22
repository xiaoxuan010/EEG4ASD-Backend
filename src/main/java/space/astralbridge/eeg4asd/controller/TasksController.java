package space.astralbridge.eeg4asd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import space.astralbridge.eeg4asd.model.Task;
import space.astralbridge.eeg4asd.model.User;
import space.astralbridge.eeg4asd.service.bussine.TasksService;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    @Autowired
    private TasksService tasksService;

    @GetMapping
    public List<Task> getTasks(HttpServletRequest request) {
        return tasksService.getAllTasks((User) request.getAttribute("authUser"));
    }
}
