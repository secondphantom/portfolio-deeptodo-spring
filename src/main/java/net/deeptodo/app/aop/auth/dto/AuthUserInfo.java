package net.deeptodo.app.aop.auth.dto;

public record AuthUserInfo(Long userId) {
    public static AuthUserInfo of(Long userId) {
        return new AuthUserInfo(userId);
    }
}
