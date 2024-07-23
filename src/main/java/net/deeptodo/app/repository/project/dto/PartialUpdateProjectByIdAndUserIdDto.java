package net.deeptodo.app.repository.project.dto;

import lombok.Builder;
import lombok.Getter;
import net.deeptodo.app.domain.Board;
import net.deeptodo.app.domain.Todo;

import java.util.List;
import java.util.Map;

public record PartialUpdateProjectByIdAndUserIdDto(
        Long projectId,
        Long userId,
        Integer version,
        String title,
        List root,
        Map<String, Board> boards,
        Map<String, Todo> todos
) {
    @Builder
    public PartialUpdateProjectByIdAndUserIdDto(Long projectId, Long userId, Integer version, String title, List root, Map<String, Board> boards, Map<String, Todo> todos) {
        this.projectId = projectId;
        this.userId = userId;
        this.version = version;
        this.title = title;
        this.root = root;
        this.boards = boards;
        this.todos = todos;
    }
}
