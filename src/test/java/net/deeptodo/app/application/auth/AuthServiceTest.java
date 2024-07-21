package net.deeptodo.app.application.auth;

import net.deeptodo.app.application.auth.dto.JwtPayload;
import net.deeptodo.app.application.auth.dto.OauthUser;
import net.deeptodo.app.application.auth.dto.response.AuthUrlServiceResponse;
import net.deeptodo.app.application.auth.dto.response.AuthUserServiceResponse;
import net.deeptodo.app.application.auth.dto.response.TokenServiceResponse;
import net.deeptodo.app.application.auth.infrastructure.jwt.JwtUtils;
import net.deeptodo.app.application.auth.interfaces.ProvidersUtils;
import net.deeptodo.app.application.auth.interfaces.TokenType;
import net.deeptodo.app.common.exception.UnauthorizedException;
import net.deeptodo.app.domain.User;
import net.deeptodo.app.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @MockBean
    private ProvidersUtils googleProviderUtils;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;


    @Test
    public void loginOauthGoogle_success() throws Exception {
        //given
        String authUrl = "authUrl";

        given(googleProviderUtils.generateAuthUrl()).willReturn(
                Optional.of(authUrl)
        );

        //when
        AuthUrlServiceResponse authUrlServiceResponse = authService.loginOauthGoogle();

        //then
        Assertions.assertThat(authUrlServiceResponse.authUrl()).isEqualTo(authUrl);
    }

    @Test
    public void loginOauthGoogleCallback_success() throws Exception {
        //given
        OauthUser oauthUser = new OauthUser("userId", "name", "email");

        given(googleProviderUtils.getOauthUser("code")).willReturn(
                Optional.of(oauthUser)
        );
        //when
        TokenServiceResponse tokenServiceResponse = authService.loginOauthGoogleCallback("code");

        //then
        Assertions.assertThat(tokenServiceResponse.accessToken()).isNotNull();
        Assertions.assertThat(tokenServiceResponse.refreshToken()).isNotNull();
        Assertions.assertThat(tokenServiceResponse.accessTokenMaxAge()).isNotNull();
        Assertions.assertThat(tokenServiceResponse.refreshTokenMaxAge()).isNotNull();
        String jwtRegex = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$";
        Assertions.assertThat(tokenServiceResponse.accessToken()).matches(jwtRegex);
        Assertions.assertThat(tokenServiceResponse.refreshToken()).matches(jwtRegex);
    }

    @Test
    public void verifyAccessToken_success() throws Exception {
        //given
        JwtPayload payload = new JwtPayload(1L);
        String accessToken = jwtUtils.generateToken(LocalDateTime.now(), TokenType.ACCESS, payload);

        //when
        AuthUserServiceResponse authUserServiceResponse = authService.verifyAccessToken(accessToken);

        //then
        Assertions.assertThat(authUserServiceResponse.userId()).isEqualTo(payload.userId());
    }

    @Test
    public void verifyAccessToken_fail_expired_token() throws Exception {
        //given
        JwtPayload payload = new JwtPayload(1L);
        String token = jwtUtils.generateToken(LocalDateTime.now().minusHours(24), TokenType.ACCESS, payload);

        //when & then
        Assertions.assertThatThrownBy(() -> {
            authService.verifyAccessToken(token);
        }).isInstanceOf(UnauthorizedException.class);

    }

    @Test
    public void verifyRefreshToken_success() throws Exception {
        //given
        User user = User.builder().id(1L).build();
        userRepository.create(user);
        JwtPayload payload = new JwtPayload(user.getId());
        String token = jwtUtils.generateToken(LocalDateTime.now(), TokenType.REFRESH, payload);

        //when
        TokenServiceResponse tokenServiceResponse = authService.verifyRefreshToken(token);

        //then
        Assertions.assertThat(tokenServiceResponse.accessToken()).isNotNull();
        Assertions.assertThat(tokenServiceResponse.refreshToken()).isNotNull();
        Assertions.assertThat(tokenServiceResponse.accessTokenMaxAge()).isNotNull();
        Assertions.assertThat(tokenServiceResponse.refreshTokenMaxAge()).isNotNull();
        String jwtRegex = "^[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+$";
        Assertions.assertThat(tokenServiceResponse.accessToken()).matches(jwtRegex);
        Assertions.assertThat(tokenServiceResponse.refreshToken()).matches(jwtRegex);
    }

    @Test
    public void verifyRefreshToken_fail_expired_token() throws Exception {
        //given
        JwtPayload payload = new JwtPayload(1L);
        String token = jwtUtils.generateToken(LocalDateTime.now().minusMonths(24), TokenType.ACCESS, payload);

        //when & then
        Assertions.assertThatThrownBy(() -> {
            authService.verifyRefreshToken(token);
        }).isInstanceOf(UnauthorizedException.class);

    }

    @Test
    public void verifyRefreshToken_fail_user_not_found() throws Exception {
        //given
        JwtPayload payload = new JwtPayload(1L);
        String token = jwtUtils.generateToken(LocalDateTime.now(), TokenType.ACCESS, payload);

        //when & then
        Assertions.assertThatThrownBy(() -> {
            authService.verifyRefreshToken(token);
        }).isInstanceOf(UnauthorizedException.class);

    }

}