package net.deeptodo.app.api.project.dto.response;

public record CreateProjectResponse(
        Long projectId
) {
    static public CreateProjectResponse of(Long projectId) {
        return new CreateProjectResponse(projectId);
    }
}
