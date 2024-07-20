package net.deeptodo.app.application.auth.dto.response;

public record AuthUserResponse(Long userId) {

    public static AuthUserResponse of(Long userId
    ) {
        return new AuthUserResponse(
                userId
        );
    }
}
