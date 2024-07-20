package net.deeptodo.app.application.auth.interfaces;

import net.deeptodo.app.application.auth.dto.OauthUser;

import java.util.Optional;

public interface ProvidersUtils {

    public Optional<String> generateAuthUrl();

    public Optional<OauthUser> getOauthUser(String code);
}
