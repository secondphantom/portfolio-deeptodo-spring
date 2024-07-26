package net.deeptodo.app.api.auth.application;

import lombok.RequiredArgsConstructor;
import net.deeptodo.app.api.auth.application.interfaces.ProvidersUtils;
import net.deeptodo.app.api.auth.dto.JwtPayload;
import net.deeptodo.app.api.auth.dto.OauthUser;
import net.deeptodo.app.api.auth.dto.response.AuthUrlResponse;
import net.deeptodo.app.api.auth.dto.response.AuthUserResponse;
import net.deeptodo.app.api.auth.dto.response.TokenResponse;
import net.deeptodo.app.api.auth.exception.AuthErrorCode;
import net.deeptodo.app.api.auth.infrastructure.jwt.JwtUtils;
import net.deeptodo.app.api.auth.infrastructure.jwt.TokenType;
import net.deeptodo.app.common.exception.CommonErrorCode;
import net.deeptodo.app.common.exception.ErrorCode;
import net.deeptodo.app.common.exception.InternalSeverErrorException;
import net.deeptodo.app.common.exception.UnauthorizedException;
import net.deeptodo.app.domain.OauthServerType;
import net.deeptodo.app.domain.PlanType;
import net.deeptodo.app.domain.SubscriptionPlan;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.repository.subscription.SubscriptionPlanRepository;
import net.deeptodo.app.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final ProvidersUtils googleProviderUtils;
    private final JwtUtils jwtUtils;


    public AuthUrlResponse loginOauthGoogle() {
        String authUrl = googleProviderUtils.generateAuthUrl().orElseThrow(() ->
                new InternalSeverErrorException(ErrorCode.getErrorCode(CommonErrorCode.INTERNAL))
        );

        return new AuthUrlResponse(authUrl);
    }


    @Transactional
    public TokenResponse loginOauthGoogleCallback(String code) {
        OauthUser oauthUser = googleProviderUtils.getOauthUser(code).orElseThrow(() ->
                new InternalSeverErrorException(ErrorCode.getErrorCode(CommonErrorCode.INTERNAL))
        );

        Long userId = getUserIdByOauthUser(oauthUser);

        return TokenResponse.of(
                getAccessToken(userId),
                jwtUtils.getMaxAge(TokenType.ACCESS),
                getRefreshToken(userId),
                jwtUtils.getMaxAge(TokenType.REFRESH)
        );
    }


    private Long getUserIdByOauthUser(OauthUser oauthUser) {
        Optional<User> findUser = userRepository.getByEmail(oauthUser.email());

        if (findUser.isPresent()) {
            return findUser.get().getId();
        }

        SubscriptionPlan freePlan = subscriptionPlanRepository.getByType(PlanType.FREE);

        User newUser = User.createNewUser(
                oauthUser.name(),
                oauthUser.email(),
                oauthUser.userId(),
                OauthServerType.GOOGLE,
                freePlan
        );

        return userRepository.create(newUser);
    }

    private String getAccessToken(Long userId) {
        JwtPayload payload = new JwtPayload(userId);

        return jwtUtils.generateToken(LocalDateTime.now(), TokenType.ACCESS, payload);
    }

    private String getRefreshToken(Long userId) {
        JwtPayload payload = new JwtPayload(userId);

        return jwtUtils.generateToken(LocalDateTime.now(), TokenType.REFRESH, payload);
    }


    public AuthUserResponse verifyAccessToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new UnauthorizedException(AuthErrorCode.getErrorCode(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN));
        }

        JwtPayload jwtPayload = jwtUtils.validateToken(TokenType.ACCESS, token)
                .orElseThrow(() -> new UnauthorizedException(AuthErrorCode.getErrorCode(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN)));

        return AuthUserResponse.of(jwtPayload.userId());
    }

    @Transactional(readOnly = true)
    public TokenResponse verifyRefreshToken(String token) {

        if (token == null || token.isEmpty()) {
            throw new UnauthorizedException(AuthErrorCode.getErrorCode(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN));
        }

        JwtPayload jwtPayload = jwtUtils.validateToken(TokenType.REFRESH, token)
                .orElseThrow(() -> new UnauthorizedException(AuthErrorCode.getErrorCode(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN)));

        User findUser = userRepository.getById(jwtPayload.userId())
                .orElseThrow(() -> new UnauthorizedException(AuthErrorCode.getErrorCode(AuthErrorCode.UNAUTHORIZED_NOT_EXISTED_MEMBER)));

        return TokenResponse.of(
                getAccessToken(findUser.getId()),
                jwtUtils.getMaxAge(TokenType.ACCESS),
                getRefreshToken(findUser.getId()),
                jwtUtils.getMaxAge(TokenType.REFRESH)
        );
    }
}
