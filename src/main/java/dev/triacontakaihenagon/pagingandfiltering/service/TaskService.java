package dev.triacontakaihenagon.pagingandfiltering.service;

import dev.triacontakaihenagon.pagingandfiltering.dto.TaskFilter;
import dev.triacontakaihenagon.pagingandfiltering.dto.TaskResponse;
import dev.triacontakaihenagon.pagingandfiltering.entity.Task;
import dev.triacontakaihenagon.pagingandfiltering.repository.TaskRepository;
import dev.triacontakaihenagon.pagingandfiltering.repository.TaskSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    private static final Set<String> SORTABLE = Set.of("id", "title", "status", "priority", "createdAt");

    @Transactional(readOnly = true)
    public Page<TaskResponse> search(TaskFilter f, Pageable pageable) {
        Specification<Task> spec = Specification.allOf(
                TaskSpecs.hasStatus(f.status()),
                TaskSpecs.hasPriority(f.priority()),
                TaskSpecs.inCategory(f.categoryId()),
                TaskSpecs.titleContains(f.q()),
                TaskSpecs.createdFrom(f.createdFrom()),
                TaskSpecs.createdTo(f.createdTo()));
        return taskRepository.findAll(spec, safe(pageable)).map(TaskResponse::from);
    }

    private Pageable safe(Pageable p) {
        List<Sort.Order> orders = new ArrayList<>();
        for (Sort.Order o : p.getSort()) {
            if (!SORTABLE.contains(o.getProperty())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Cannot sort by: " + o.getProperty());
            }
            orders.add(o);
        }
        if (orders.stream().noneMatch(o -> o.getProperty().equals("id"))) {
            orders.add(Sort.Order.asc("id"));
        }
        return PageRequest.of(p.getPageNumber(), p.getPageSize(), Sort.by(orders));
    }
}