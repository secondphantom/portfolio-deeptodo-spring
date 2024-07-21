package net.deeptodo.app.aop.auth.dto;

public record AuthUser(Long userId) {
    public static  AuthUser of(Long userId) {
        return new AuthUser(userId);
    }
}
