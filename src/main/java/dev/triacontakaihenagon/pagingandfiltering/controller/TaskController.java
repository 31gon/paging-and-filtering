package dev.triacontakaihenagon.pagingandfiltering.controller;

import dev.triacontakaihenagon.pagingandfiltering.dto.TaskResponse;
import dev.triacontakaihenagon.pagingandfiltering.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public Page<TaskResponse> list(Pageable pageable) {
        return taskService.list(pageable);
    }
}