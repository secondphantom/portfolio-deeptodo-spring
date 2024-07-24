package net.deeptodo.app.api.project.dto;

import lombok.Builder;

public record Pagination(
        Integer currentPage,
        Integer pageSize
) {
    @Builder
    public Pagination {
    }
}
