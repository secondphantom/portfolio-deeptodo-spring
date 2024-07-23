package net.deeptodo.app.infrastructure.jwt;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@ConfigurationProperties("jwt")
@Validated
public class JwtProperties {
    @NotEmpty
    private String accessKey;
    @NotEmpty
    private String refreshKey;
    private Integer accessExpiredHours;
    private Integer refreshExpiredHours;

    public JwtProperties(String accessKey, String refreshKey, Integer accessExpiredHours, Integer refreshExpiredHours) {
        this.accessKey = accessKey;
        this.refreshKey = refreshKey;
        this.accessExpiredHours = accessExpiredHours;
        this.refreshExpiredHours = refreshExpiredHours;
    }

    public String findTokenKey(final TokenType tokenType) {
        if (TokenType.ACCESS == tokenType) {
            return accessKey;
        }

        return refreshKey;
    }

    public Integer findExpiredHours(final TokenType tokenType) {
        if (TokenType.ACCESS == tokenType) {
            return accessExpiredHours;
        }

        return refreshExpiredHours;
    }
}
