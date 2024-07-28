package net.deeptodo.app.common.properties;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@ConfigurationProperties("domain")
@Validated
public class DomainProperties {
    @NotEmpty
    private String serviceUrl;

    public DomainProperties(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
