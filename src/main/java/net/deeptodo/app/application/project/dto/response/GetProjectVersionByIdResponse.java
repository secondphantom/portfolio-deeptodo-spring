package net.deeptodo.app.application.project.dto.response;

public record GetProjectVersionByIdResponse(Long projectId, Integer version) {

    public static GetProjectVersionByIdResponse of(Long projectId, Integer version) {
        return new GetProjectVersionByIdResponse(projectId, version);
    }
}
