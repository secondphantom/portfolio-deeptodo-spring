package net.deeptodo.app.api.project.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QueryProjectDto {
    private Long projectId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public QueryProjectDto(Long projectId, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.projectId = projectId;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
