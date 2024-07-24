package net.deeptodo.app.api.project.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QueryProjectDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QueryProjectDto(Long id, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
