package net.deeptodo.app.application.auth.dto;

public record OauthUser(
        String userId,
        String name,
        String email
) {
}
