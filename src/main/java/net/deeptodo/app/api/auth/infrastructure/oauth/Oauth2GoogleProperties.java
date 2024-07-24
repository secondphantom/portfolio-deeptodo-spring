package net.deeptodo.app.api.auth.infrastructure.oauth;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@ConfigurationProperties("oauth2.client.providers.google")
@Validated
public class Oauth2GoogleProperties {

    @NotEmpty
    private String clientId;
    @NotEmpty
    private String clientSecret;
    @NotEmpty
    private String redirectUrl;

    public Oauth2GoogleProperties
            (String clientId, String clientSecret, String redirectUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
    }
}
