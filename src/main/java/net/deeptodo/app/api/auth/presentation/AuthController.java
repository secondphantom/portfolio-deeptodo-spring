package net.deeptodo.app.api.auth.presentation;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.deeptodo.app.api.auth.application.AuthService;
import net.deeptodo.app.api.auth.dto.response.AuthUrlResponse;
import net.deeptodo.app.api.auth.dto.response.AuthUserResponse;
import net.deeptodo.app.api.auth.dto.response.TokenResponse;
import net.deeptodo.app.common.infrastructure.CookieUtils;
import net.deeptodo.app.common.properties.DomainProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final DomainProperties domainProperties;

    @GetMapping("/login/oauth/google")
    public ResponseEntity<AuthUrlResponse> loginOauthGoogle(
            HttpServletResponse response
    ) throws IOException {

        AuthUrlResponse authUrlResponse = authService.loginOauthGoogle();

        return ResponseEntity.ok(authUrlResponse);
    }


    @GetMapping("/login/oauth/google/callback")
    public ResponseEntity<Void> loginOauthGoogleCallback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) throws IOException {

        TokenResponse tokenResponse = authService.loginOauthGoogleCallback(code);

        CookieUtils.addCookie(response, "access_token", tokenResponse.accessToken(), tokenResponse.accessTokenMaxAge());
        CookieUtils.addCookie(response, "refresh_token", tokenResponse.refreshToken(), tokenResponse.refreshTokenMaxAge());

        response.sendRedirect(domainProperties.getServiceUrl());

        return ResponseEntity.ok().build();
    }


    @GetMapping("/verify-access-token")
    public ResponseEntity<AuthUserResponse> verifyAccessToken(
            @CookieValue(value = "access_token", required = false) String accessToken
    ) {

        AuthUserResponse authUserResponse = authService.verifyAccessToken(accessToken);

        return ResponseEntity.ok(authUserResponse);
    }

    @PostMapping("/refresh-access-token")
    public ResponseEntity<Void> refreshAccessToken(
            @CookieValue(value = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response
    ) {

        TokenResponse tokenResponse = authService.verifyRefreshToken(refreshToken);

        CookieUtils.addCookie(response, "access_token", tokenResponse.accessToken(), tokenResponse.accessTokenMaxAge());
        CookieUtils.addCookie(response, "refresh_token", tokenResponse.refreshToken(), tokenResponse.refreshTokenMaxAge());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut(
            HttpServletResponse response
    ) {

        CookieUtils.addCookie(response, "access_token", null, 0);
        CookieUtils.addCookie(response, "refresh_token", null, 0);

        return ResponseEntity.ok().build();
    }

}
