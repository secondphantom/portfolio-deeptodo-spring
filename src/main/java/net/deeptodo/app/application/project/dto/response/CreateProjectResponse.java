package net.deeptodo.app.application.project.dto.response;

public record CreateProjectResponse(
        Long projectId
) {
    static public CreateProjectResponse of(Long projectId) {
        return new CreateProjectResponse(projectId);
    }
}
