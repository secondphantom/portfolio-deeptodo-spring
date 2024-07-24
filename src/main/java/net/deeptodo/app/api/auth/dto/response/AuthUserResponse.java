package net.deeptodo.app.api.auth.dto.response;

public record AuthUserResponse(Long userId) {

    public static AuthUserResponse of(Long userId
    ) {
        return new AuthUserResponse(
                userId
        );
    }
}
