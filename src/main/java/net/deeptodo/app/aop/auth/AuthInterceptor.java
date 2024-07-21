package net.deeptodo.app.aop.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.deeptodo.app.aop.auth.dto.AuthUser;
import net.deeptodo.app.application.auth.AuthService;
import net.deeptodo.app.application.auth.dto.response.AuthUserResponse;
import net.deeptodo.app.application.auth.exception.AuthErrorCode;
import net.deeptodo.app.common.exception.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        String accessToken = Optional.ofNullable(request.getCookies())
                .map(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> "access_token".equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElseThrow(() -> new UnauthorizedException(AuthErrorCode.getErrorCode(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN))))
                .orElseThrow(() -> new UnauthorizedException(AuthErrorCode.getErrorCode(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN)));
        System.out.println("accessToken = " + accessToken);
        AuthUserResponse authUserResponse = authService.verifyAccessToken(accessToken);

        System.out.println("authUserResponse = " + authUserResponse);
        request.setAttribute("authUser", new AuthUser(authUserResponse.userId()));

        return true;
    }
}
