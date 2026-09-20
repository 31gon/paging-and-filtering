package dev.triacontakaihenagon.pagingandfiltering.seed;

import dev.triacontakaihenagon.pagingandfiltering.entity.*;
import dev.triacontakaihenagon.pagingandfiltering.repository.CategoryRepository;
import dev.triacontakaihenagon.pagingandfiltering.repository.LabelRepository;
import dev.triacontakaihenagon.pagingandfiltering.repository.TaskRepository;
import dev.triacontakaihenagon.pagingandfiltering.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class DataSeeder implements ApplicationRunner {
    private static final int TASKS = 100_000;

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LabelRepository labelRepository;
    private final JdbcTemplate jdbc;

    public DataSeeder(
            TaskRepository taskRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            LabelRepository labelRepository,
            JdbcTemplate jdbc
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.labelRepository = labelRepository;
        this.jdbc = jdbc;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (taskRepository.count() > 0) return;

        List<Long> userIds = IntStream.rangeClosed(1, 20).mapToObj(i -> {
            User u = new User();
            u.setUserName("user" + i);
            u.setPassword("x");
            return userRepository.save(u).getId();
        }).toList();

        List<Long> categoryIds = IntStream.rangeClosed(1, 10).mapToObj(i -> {
            Category c = new Category();
            c.setName("category" + i);
            return categoryRepository.save(c).getId();
        }).toList();

        List<Long> labelIds = IntStream.rangeClosed(1, 15).mapToObj(i -> {
            Label l = new Label();
            l.setName("label" + i);
            return labelRepository.save(l).getId();
        }).toList();

        Random rnd = new Random(42);
        TaskStatus[] statuses = TaskStatus.values();
        TaskPriority[] priorities = TaskPriority.values();
        LocalDateTime now = LocalDateTime.now();

        List<Object[]> tasks = IntStream.rangeClosed(1, TASKS).mapToObj(i -> new Object[]{
                (long) i,
                "Task " + i + " " + (rnd.nextBoolean() ? "fix bug" : "write docs"),
                statuses[rnd.nextInt(statuses.length)].name(),
                priorities[rnd.nextInt(priorities.length)].name(),
                Timestamp.valueOf(now.minusMinutes(rnd.nextInt(60 * 24 * 365))),
                userIds.get(rnd.nextInt(userIds.size())),
                categoryIds.get(rnd.nextInt(categoryIds.size()))
        }).toList();

        jdbc.batchUpdate("insert into tasks (id, title, status, priority, created_at, user_id, category_id) values (?,?,?,?,?,?,?)", tasks);

        List<Object[]> links = IntStream.rangeClosed(1, TASKS)
                .boxed()
                .flatMap(id -> IntStream.range(0, rnd.nextInt(3))
                        .mapToObj(k -> new Object[]{(long) id, labelIds.get((id + k * 5) % labelIds.size())}))
                .toList();

        jdbc.batchUpdate("insert into task_labels (task_id, label_id) values (?,?)", links);
    }
}