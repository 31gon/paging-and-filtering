package dev.triacontakaihenagon.pagingandfiltering.dto;

import dev.triacontakaihenagon.pagingandfiltering.entity.Label;
import dev.triacontakaihenagon.pagingandfiltering.entity.Task;
import dev.triacontakaihenagon.pagingandfiltering.entity.TaskPriority;
import dev.triacontakaihenagon.pagingandfiltering.entity.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TaskResponse(
        Long id,
        String title,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime createdAt,
        Long userId,
        Long categoryId,
        List<Long> labelIds
) {
    public static TaskResponse from(Task t) {
        return new TaskResponse(
                t.getId(),
                t.getTitle(),
                t.getStatus(),
                t.getPriority(),
                t.getCreatedAt(),
                t.getUser() == null ? null : t.getUser().getId(),
                t.getCategory() == null ? null : t.getCategory().getId(),
                t.getLabels().stream().map(Label::getId).toList()
        );
    }
}