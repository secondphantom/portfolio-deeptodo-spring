package net.deeptodo.app.infrastructure.jwt;

import net.deeptodo.app.application.auth.dto.JwtPayload;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

class JwtUtilsTest {
    private JwtProperties jwtProperties = new JwtProperties("thisistoolargeaccesstokenkeyfordummykeydataforlocal", "thisistoolargeaccesstokenkeyfordummykeydataforlocal", 1, 12);
    private JwtUtils jwtUtils = new JwtUtils(jwtProperties);

    @Test
    public void generateToken() {
        //given
        JwtPayload payload = new JwtPayload(1L);

        //when
        String accessToken = jwtUtils.generateToken(LocalDateTime.now(), TokenType.ACCESS, payload);

        //then
        Assertions.assertThat(accessToken).asString();
    }


    @Test
    public void validateToken_success() throws Exception {
        //given
        JwtPayload payload = new JwtPayload(1L);
        String accessToken = jwtUtils.generateToken(LocalDateTime.now(), TokenType.ACCESS, payload);

        //when
        Optional<JwtPayload> jwtPayload = jwtUtils.validateToken(TokenType.ACCESS, accessToken);

        //then
        Assertions.assertThat(jwtPayload).isPresent();
        Assertions.assertThat(jwtPayload.get().userId()).isEqualTo(payload.userId());
    }

    @Test
    public void validateToken_fail() throws Exception {
        //given
        JwtPayload payload = new JwtPayload(1L);
        String accessToken = jwtUtils.generateToken(LocalDateTime.now().minusHours(24), TokenType.ACCESS, payload);

        //when
        Optional<JwtPayload> jwtPayload = jwtUtils.validateToken(TokenType.ACCESS, accessToken);

        //then
        Assertions.assertThat(jwtPayload).isNotPresent();
    }

}