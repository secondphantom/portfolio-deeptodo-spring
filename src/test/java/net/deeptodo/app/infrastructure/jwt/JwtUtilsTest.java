package net.deeptodo.app.infrastructure.jwt;

import net.deeptodo.app.api.auth.dto.JwtPayload;
import net.deeptodo.app.api.auth.infrastructure.jwt.JwtProperties;
import net.deeptodo.app.api.auth.infrastructure.jwt.JwtUtils;
import net.deeptodo.app.api.auth.infrastructure.jwt.TokenType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

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