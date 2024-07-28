package net.deeptodo.app.api.project.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

public record CreateProjectRequest(
        @NotEmpty
        String title
) {
        @Builder
        public CreateProjectRequest {
        }
}
