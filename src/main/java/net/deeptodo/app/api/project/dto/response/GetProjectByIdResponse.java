package net.deeptodo.app.api.project.dto.response;

import lombok.Builder;
import net.deeptodo.app.domain.Project;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record GetProjectByIdResponse
        (
                Long projectId,
                Integer version,
                String title,
                List<?> root,
                Map<String, RecordBoard> boards,
                Map<String, RecordTodo> todos,
                boolean enabled,
                LocalDateTime createdAt,
                LocalDateTime updatedAt
        ) {


    @Builder
    public GetProjectByIdResponse {
    }

    public static GetProjectByIdResponse fromProject(Project project) {
        return GetProjectByIdResponse.builder()
                .projectId(project.getId())
                .version(project.getVersion())
                .title(project.getTitle())
                .root(project.getRoot())
                .boards(project.getBoards().entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new RecordBoard(entry.getValue().getTitle())
                        )))
                .todos(project.getTodos().entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new RecordTodo(
                                        entry.getValue().getTodoId(),
                                        entry.getValue().getTitle(),
                                        entry.getValue().isDone(),
                                        entry.getValue().isExpand(),
                                        entry.getValue().isEnableCalendar(),
                                        entry.getValue().isSyncGoogleCalendar(),
                                        entry.getValue().getStartDate(),
                                        entry.getValue().getEndDate()
                                )
                        )))
                .enabled(project.isEnabled())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    public record RecordBoard(
            String title
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


