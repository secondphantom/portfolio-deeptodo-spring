package net.deeptodo.app.api.project.dto.response;

import lombok.Builder;
import net.deeptodo.app.api.project.dto.Pagination;
import net.deeptodo.app.api.project.dto.QueryProjectDto;

import java.util.List;

public record GetProjectsByQueryResponse(
        List<QueryProjectDto> projects,
        Pagination pagination
) {

    @Builder
    public GetProjectsByQueryResponse {
    }

    public static GetProjectsByQueryResponse of(
            List<QueryProjectDto> projects,
            Pagination pagination
    ) {
        return GetProjectsByQueryResponse.builder()
                .projects(projects)
                .pagination(pagination)
                .build();
    }
}
