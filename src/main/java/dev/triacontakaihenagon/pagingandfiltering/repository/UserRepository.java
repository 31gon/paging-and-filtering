package dev.triacontakaihenagon.pagingandfiltering.repository;

import dev.triacontakaihenagon.pagingandfiltering.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
