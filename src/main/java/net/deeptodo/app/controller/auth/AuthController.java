package net.deeptodo.app.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.deeptodo.app.application.auth.AuthService;
import net.deeptodo.app.application.auth.dto.response.AuthUrlServiceResponse;
import net.deeptodo.app.application.auth.dto.response.AuthUserServiceResponse;
import net.deeptodo.app.application.auth.dto.response.TokenServiceResponse;
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

        AuthUrlServiceResponse authUrlServiceResponse = authService.loginOauthGoogle();
        response.sendRedirect(authUrlServiceResponse.authUrl());

        return ResponseEntity.ok().build();
    }


    @GetMapping("/login/oauth/google/callback")
    public ResponseEntity<Void> loginOauthGoogleCallback(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) {

        TokenServiceResponse tokenServiceResponse = authService.loginOauthGoogleCallback(code);

        CookieUtils.addCookie(response, "access_token", tokenServiceResponse.accessToken(), tokenServiceResponse.accessTokenMaxAge());
        CookieUtils.addCookie(response, "refresh_token", tokenServiceResponse.refreshToken(), tokenServiceResponse.refreshTokenMaxAge());

        return ResponseEntity.ok().build();
    }


    @GetMapping("/verify-access-token")
    public ResponseEntity<VerifyAccessTokenResponse> verifyAccessToken(
            @CookieValue(value = "access_token", required = false) String accessToken
    ) {

        AuthUserServiceResponse authUserServiceResponse = authService.verifyAccessToken(accessToken);

        return ResponseEntity.ok(VerifyAccessTokenResponse.of(authUserServiceResponse.userId()));
    }

    @PostMapping("/refresh-access-token")
    public ResponseEntity<Void> refreshAccessToken(
            @CookieValue(value = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response
    ) {

        TokenServiceResponse tokenServiceResponse = authService.verifyRefreshToken(refreshToken);

        CookieUtils.addCookie(response, "access_token", tokenServiceResponse.accessToken(), tokenServiceResponse.accessTokenMaxAge());
        CookieUtils.addCookie(response, "refresh_token", tokenServiceResponse.refreshToken(), tokenServiceResponse.refreshTokenMaxAge());

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
