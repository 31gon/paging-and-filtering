package dev.triacontakaihenagon.pagingandfiltering.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@Entity
@Table(name = "users")
public class User {
    public User() {}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String password;//Hash later I guess
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();
}
