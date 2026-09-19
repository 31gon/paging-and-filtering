package dev.triacontakaihenagon.pagingandfiltering.repository;

import dev.triacontakaihenagon.pagingandfiltering.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
