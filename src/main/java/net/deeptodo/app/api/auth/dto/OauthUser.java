package net.deeptodo.app.api.auth.dto;

public record OauthUser(
        String userId,
        String name,
        String email
) {
}
