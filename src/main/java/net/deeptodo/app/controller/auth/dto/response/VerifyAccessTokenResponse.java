package net.deeptodo.app.controller.auth.dto.response;

public record VerifyAccessTokenResponse(Long userId) {
    public static VerifyAccessTokenResponse of(Long userId
    ) {
        return new VerifyAccessTokenResponse(
                userId
        );
    }
}
