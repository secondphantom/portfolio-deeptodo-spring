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
                Long userId,
                Integer version,
                String title,
                List<?> root,
                Map<String, RecordBoard> boards,
                Map<String, RecordTodo> todos,
                LocalDateTime createdAt,
                LocalDateTime updatedAt
        ) {


    @Builder
    public GetProjectByIdResponse {
    }

    public static GetProjectByIdResponse fromProject(Project project) {
        return GetProjectByIdResponse.builder()
                .projectId(project.getId())
                .userId(project.getUser().getId())
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
                                        entry.getValue().isDone(),
                                        entry.getValue().getTitle(),
                                        entry.getValue().isExpand(),
                                        entry.getValue().getTodoId(),
                                        entry.getValue().getStartDate(),
                                        entry.getValue().getEndDate(),
                                        entry.getValue().isEnableCalendar(),
                                        entry.getValue().isSyncGoogleCalendar()
                                )
                        )))
                .build();
    }

    private record RecordBoard(
            String title
    ) {
    }

    private record RecordTodo(
            boolean done,
            String title,
            boolean expand,
            String todoId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            boolean enableCalendar,
            boolean syncGoogleCalendar
    ) {
    }


}


