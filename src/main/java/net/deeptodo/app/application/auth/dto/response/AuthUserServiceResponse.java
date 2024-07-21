package net.deeptodo.app.application.auth.dto.response;

public record AuthUserServiceResponse(Long userId) {

    public static AuthUserServiceResponse of(Long userId
    ) {
        return new AuthUserServiceResponse(
                userId
        );
    }
}
