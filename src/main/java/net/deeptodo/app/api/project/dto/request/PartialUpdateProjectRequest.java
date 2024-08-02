package net.deeptodo.app.api.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record PartialUpdateProjectRequest(
        @NotNull(message = "Need version field")
        Integer version,
        String title,
        List<?> root,
        Map<String, RecordBoard> boards,
        Map<String, RecordTodo> todos
) {
    @Builder
    public PartialUpdateProjectRequest(
            Integer version,
            String title,
            List<?> root,
            Map<String, RecordBoard> boards,
            Map<String, RecordTodo> todos
    ) {
        this.version = version;
        this.title = title;
        this.root = root;
        this.boards = boards;
        this.todos = todos;
    }

    public record RecordBoard(
            String boardId,
            String title,
            boolean fold
    ) {
        @Builder
        public RecordBoard {
        }
    }

    public record RecordTodo(
            String todoId,
            String title,
            boolean done,
            boolean expand,
            boolean enableCalendar,
            boolean syncGoogleCalendar,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        @Builder
        public RecordTodo {
        }
    }
}
