package net.deeptodo.app.api.project.dto.response;

public record GetProjectVersionAndEnabledByIdResponse(Long projectId, Integer version, boolean enabled) {

    public static GetProjectVersionAndEnabledByIdResponse of(Long projectId, Integer version, boolean enabled) {
        return new GetProjectVersionAndEnabledByIdResponse(projectId, version, enabled);
    }
}
