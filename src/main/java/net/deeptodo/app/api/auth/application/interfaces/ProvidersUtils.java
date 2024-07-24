package net.deeptodo.app.api.auth.application.interfaces;

import net.deeptodo.app.api.auth.dto.OauthUser;

import java.util.Optional;

public interface ProvidersUtils {

    Optional<String> generateAuthUrl();

    Optional<OauthUser> getOauthUser(String code);
}
