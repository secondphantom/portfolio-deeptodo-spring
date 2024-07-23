package net.deeptodo.app.application.auth.dto.response;

public record TokenResponse(
        String accessToken,
        Integer accessTokenMaxAge,
        String refreshToken,
        Integer refreshTokenMaxAge
) {

    public static TokenResponse of(String accessToken,
                                   Integer accessTokenMaxAge,
                                   String refreshToken,
                                   Integer refreshTokenMaxAge) {
        return new TokenResponse(
                accessToken,
                accessTokenMaxAge,
                refreshToken,
                refreshTokenMaxAge
        );
    }
}
