package net.deeptodo.app.infrastructure.interfaces;

import net.deeptodo.app.application.auth.dto.OauthUser;

import java.util.Optional;

public interface ProvidersUtils {

    Optional<String> generateAuthUrl();

    Optional<OauthUser> getOauthUser(String code);
}
