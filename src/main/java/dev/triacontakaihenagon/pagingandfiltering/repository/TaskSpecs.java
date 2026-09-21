package dev.triacontakaihenagon.pagingandfiltering.repository;

import dev.triacontakaihenagon.pagingandfiltering.entity.Task;
import dev.triacontakaihenagon.pagingandfiltering.entity.TaskPriority;
import dev.triacontakaihenagon.pagingandfiltering.entity.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class TaskSpecs {
    private TaskSpecs() {}

    public static Specification<Task> hasStatus(TaskStatus s) {
        return (root, query, cb) -> s == null ? null : cb.equal(root.get("status"), s);
    }

    public static Specification<Task> hasPriority(TaskPriority p) {
        return (root, query, cb) -> p == null ? null : cb.equal(root.get("priority"), p);
    }

    public static Specification<Task> inCategory(Long id) {
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("category").get("id"), id);
    }

    public static Specification<Task> titleContains(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isBlank()) return null;
            String escaped = text.toLowerCase()
                    .replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
            return cb.like(cb.lower(root.get("title")), "%" + escaped + "%", '\\');
        };
    }

    public static Specification<Task> createdFrom(LocalDate d) {
        return (root, query, cb) -> d == null ? null
                : cb.greaterThanOrEqualTo(root.get("createdAt"), d.atStartOfDay());
    }

    public static Specification<Task> createdTo(LocalDate d) {
        return (root, query, cb) -> d == null ? null
                : cb.lessThan(root.get("createdAt"), d.plusDays(1).atStartOfDay());
    }
}