package net.deeptodo.app.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "projects")
@ToString
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer version;

    private String title;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<?> root;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Board> boards;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Todo> todos;

    private boolean enabled;

    @Builder
    public Project(Long id, User user, Integer version, String title, List<?> root, Map<String, Board> boards, Map<String, Todo> todos, boolean enabled) {
        this.id = id;
        this.user = user;
        this.version = version;
        this.title = title;
        this.root = root;
        this.boards = boards;
        this.todos = todos;
        this.enabled = enabled;
    }

    static public Project createNewProject(
            User user
    ) {
        return Project.builder()
                .user(user)
                .title("")
                .version(0)
                .root(new ArrayList<>())
                .boards(new HashMap<>())
                .todos(new HashMap<>())
                .enabled(true)
                .build();
    }


}
