package net.deeptodo.app.api.auth.infrastructure.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.deeptodo.app.api.auth.application.interfaces.ProvidersUtils;
import net.deeptodo.app.api.auth.dto.OauthUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GoogleProviderUtils implements ProvidersUtils {

    private final Oauth2GoogleProperties oauth2GoogleProperties;

    public Optional<String> generateAuthUrl() {

        try {
            String scope = "email profile";
            String responseType = "code";
            String authUrl = "https://accounts.google.com/o/oauth2/v2/auth";

            String url = String.format("%s?response_type=%s&client_id=%s&redirect_uri=%s&scope=%s",
                    authUrl,
                    responseType,
                    URLEncoder.encode(oauth2GoogleProperties.getClientId(), "UTF-8"),
                    URLEncoder.encode(oauth2GoogleProperties.getRedirectUrl(), "UTF-8"),
                    URLEncoder.encode(scope, "UTF-8"));
            return Optional.of(url);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<OauthUser> getOauthUser(String code) {

        try {
            // 인증 코드를 액세스 토큰으로 교환
            String tokenResponse = exchangeCodeForAccessToken(code);

            // 액세스 토큰 파싱
            String accessToken = parseAccessToken(tokenResponse);

            // 액세스 토큰을 사용하여 사용자 정보 요청
            String userInfo = getUserInfo(accessToken);

            // 사용자 정보를 파싱하여 출력
            OauthUser oauthUser = parseUserInfo(userInfo);

            return Optional.of(oauthUser);
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    public String exchangeCodeForAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        String body = String.format("code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
                code, oauth2GoogleProperties.getClientId(), oauth2GoogleProperties.getClientSecret(), oauth2GoogleProperties.getRedirectUrl());

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        String TOKEN_URL = "https://oauth2.googleapis.com/token";

        ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    private String parseAccessToken(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        return rootNode.path("access_token").asText();
    }

    private String getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        String USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

        ResponseEntity<String> response = restTemplate.exchange(USER_INFO_URL, HttpMethod.GET, request, String.class);
        return response.getBody();
    }


    private OauthUser parseUserInfo(String json) throws Exception {
        System.out.println("json = " + json);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);
        String userId = rootNode.path("sub").asText();
        String name = rootNode.path("name").asText();
        String email = rootNode.path("email").asText();
        String avatarUrl = rootNode.path("picture").asText();

        return new OauthUser(userId, name, email,avatarUrl);
    }

}
