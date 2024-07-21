package net.deeptodo.app.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.deeptodo.app.application.auth.AuthService;
import net.deeptodo.app.application.auth.dto.response.AuthUrlResponse;
import net.deeptodo.app.application.auth.dto.response.AuthUserResponse;
import net.deeptodo.app.application.auth.dto.response.TokenResponse;
import net.deeptodo.app.common.infrastructure.CookieUtils;
import net.deeptodo.app.controller.auth.dto.response.VerifyAccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/oauth/google")
    public ResponseEntity<Void> loginOauthGoogle(
            HttpServletResponse response
    ) throws IOException {

        AuthUrlResponse authUrlResponse = authService.loginOauthGoogle();
        response.sendRedirect(authUrlResponse.authUrl());

        return ResponseEntity.ok().build();
    }


    @GetMapping("/login/oauth/google/callback")
    public ResponseEntity<Void> loginOauthGoogleCallback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) {

        TokenResponse tokenResponse = authService.loginOauthGoogleCallback(code);

        CookieUtils.addCookie(response, "access_token", tokenResponse.accessToken(), tokenResponse.accessTokenMaxAge());
        CookieUtils.addCookie(response, "refresh_token", tokenResponse.refreshToken(), tokenResponse.refreshTokenMaxAge());

        return ResponseEntity.ok().build();
    }


    @GetMapping("/verify-access-token")
    public ResponseEntity<VerifyAccessTokenResponse> verifyAccessToken(
            @CookieValue(value = "access_token", required = false) String accessToken
    ) {

        AuthUserResponse authUserResponse = authService.verifyAccessToken(accessToken);

        return ResponseEntity.ok(VerifyAccessTokenResponse.of(authUserResponse.userId()));
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
