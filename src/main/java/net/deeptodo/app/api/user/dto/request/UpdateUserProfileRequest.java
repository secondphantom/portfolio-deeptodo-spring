package net.deeptodo.app.api.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record UpdateUserProfileRequest(
        @NotNull
        String nickName,
        @NotNull
        String avatarUrl
) {
    @Builder
    public UpdateUserProfileRequest {
    }
}
