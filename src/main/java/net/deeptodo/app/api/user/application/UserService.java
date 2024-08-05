package net.deeptodo.app.api.user.application;

import lombok.RequiredArgsConstructor;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.user.dto.request.UpdateUserProfileRequest;
import net.deeptodo.app.api.user.dto.response.GetUserProfileResponse;
import net.deeptodo.app.api.user.exception.UserErrorCode;
import net.deeptodo.app.common.exception.UnauthorizedException;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public GetUserProfileResponse getUserProfile(AuthUserInfo authUserInfo) {
        User user = userRepository.getById(authUserInfo.userId())
                .orElseThrow(() -> new UnauthorizedException(UserErrorCode.getErrorCode(UserErrorCode.UNAUTHORIZED_NOT_FOUND_MEMBER)));

        return GetUserProfileResponse.fromUser(user);
    }

    @Transactional
    public void updateUserProfile(AuthUserInfo authUserInfo, UpdateUserProfileRequest request) {
        User user = userRepository.getById(authUserInfo.userId())
                .orElseThrow(() -> new UnauthorizedException(UserErrorCode.getErrorCode(UserErrorCode.UNAUTHORIZED_NOT_FOUND_MEMBER)));
        user.updateProfile(request.nickName(), request.avatarUrl());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(AuthUserInfo authUserInfo) {
        User user = userRepository.getById(authUserInfo.userId())
                .orElseThrow(() -> new UnauthorizedException(UserErrorCode.getErrorCode(UserErrorCode.UNAUTHORIZED_NOT_FOUND_MEMBER)));
        userRepository.delete(user);
    }
}
