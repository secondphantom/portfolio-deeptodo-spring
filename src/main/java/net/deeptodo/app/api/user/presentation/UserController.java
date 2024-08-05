package net.deeptodo.app.api.user.presentation;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.deeptodo.app.aop.auth.AuthUser;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.user.application.UserService;
import net.deeptodo.app.api.user.dto.request.UpdateUserProfileRequest;
import net.deeptodo.app.api.user.dto.response.GetUserProfileResponse;
import net.deeptodo.app.common.infrastructure.CookieUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<GetUserProfileResponse> getUserProfile(
            @AuthUser AuthUserInfo authUserInfo
    ) {
        GetUserProfileResponse response = userService.getUserProfile(authUserInfo);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateUserProfile(
            @AuthUser AuthUserInfo authUserInfo,
            @RequestBody @Valid UpdateUserProfileRequest request
    ) {
        userService.updateUserProfile(authUserInfo, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            HttpServletResponse response,
            @AuthUser AuthUserInfo authUserInfo
    ) {
        userService.deleteUser(authUserInfo);
        CookieUtils.addCookie(response, "access_token", null, 0);
        CookieUtils.addCookie(response, "refresh_token", null, 0);
        return ResponseEntity.noContent().build();
    }
}
