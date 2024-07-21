package net.deeptodo.app.application.auth.dto.response;

public record TokenServiceResponse(
        String accessToken,
        Integer accessTokenMaxAge,
        String refreshToken,
        Integer refreshTokenMaxAge
) {

    public static TokenServiceResponse of(String accessToken,
                                          Integer accessTokenMaxAge,
                                          String refreshToken,
                                          Integer refreshTokenMaxAge) {
        return new TokenServiceResponse(
                 accessToken,
                 accessTokenMaxAge,
                 refreshToken,
                 refreshTokenMaxAge
        );
    }
}
