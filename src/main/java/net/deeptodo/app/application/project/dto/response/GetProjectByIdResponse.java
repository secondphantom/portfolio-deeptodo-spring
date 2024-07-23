package net.deeptodo.app.application.project.dto.response;

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
                String title,
                List root,
                Map<String, Board> boards,
                Map<String, Todo> todos,
                LocalDateTime createdAt,
                LocalDateTime updatedAt
        ) {


    @Builder
    public GetProjectByIdResponse(Long projectId, Long userId, String title, List root, Map<String, Board> boards, Map<String, Todo> todos, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.projectId = projectId;
        this.userId = userId;
        this.title = title;
        this.root = root;
        this.boards = boards;
        this.todos = todos;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GetProjectByIdResponse createResponseByProject(Project project) {
        return GetProjectByIdResponse.builder()
                .projectId(project.getId())
                .userId(project.getUser().getId())
                .title(project.getTitle())
                .root(project.getRoot())
                .boards(project.getBoards().entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new Board(entry.getValue().getTitle())
                        )))
                .todos(project.getTodos().entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> new Todo(
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

    private record Board(
            String title
    ) {
    }

    private record Todo(
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


