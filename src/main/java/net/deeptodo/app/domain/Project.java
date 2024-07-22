package net.deeptodo.app.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;

    private String root;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Board> boards;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Todo> todos;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Project(Long id, User user, String title, String root, Map<String, Board> boards, Map<String, Todo> todos, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.root = root;
        this.boards = boards;
        this.todos = todos;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
