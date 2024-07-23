package net.deeptodo.app.application.project.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import net.deeptodo.app.domain.Board;
import net.deeptodo.app.domain.Todo;

import java.util.List;
import java.util.Map;

public record PartialUpdateProjectRequest (
    @NotEmpty(message = "Need version field")
    Integer version,
    String title,
    List root,
    Map<String, Board> boards,
    Map<String, Todo> todos
) {
    @Builder
    public PartialUpdateProjectRequest(Integer version, String title, List root, Map<String, Board> boards, Map<String, Todo> todos) {
        this.version = version;
        this.title = title;
        this.root = root;
        this.boards = boards;
        this.todos = todos;
    }
}
