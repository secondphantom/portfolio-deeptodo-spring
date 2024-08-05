package net.deeptodo.app.api.user.dto.response;

import lombok.Builder;
import net.deeptodo.app.domain.User;

public record GetUserProfileResponse(
        String nickName,
        String email,
        String avatarUrl
) {
    @Builder
    public GetUserProfileResponse {
    }

    public static GetUserProfileResponse fromUser(User user) {
        return new GetUserProfileResponse(
                user.getNickName(),
                user.getEmail(),
                user.getAvatarUrl()
        );
    }
}
