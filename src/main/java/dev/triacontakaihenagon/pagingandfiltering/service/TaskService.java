package dev.triacontakaihenagon.pagingandfiltering.service;

import dev.triacontakaihenagon.pagingandfiltering.dto.TaskResponse;
import dev.triacontakaihenagon.pagingandfiltering.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> list(Pageable pageable) {
        return taskRepository.findAll(pageable).map(TaskResponse::from);
    }
}