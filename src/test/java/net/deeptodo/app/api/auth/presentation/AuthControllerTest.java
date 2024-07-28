package net.deeptodo.app.api.auth.presentation;

import jakarta.servlet.http.Cookie;
import net.deeptodo.app.api.RestDocsIntegration;
import net.deeptodo.app.api.auth.application.AuthService;
import net.deeptodo.app.api.auth.dto.response.AuthUrlResponse;
import net.deeptodo.app.api.auth.dto.response.AuthUserResponse;
import net.deeptodo.app.api.auth.dto.response.TokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class AuthControllerTest extends RestDocsIntegration {


    @MockBean
    private AuthService authService;

    @Test
    public void loginOauthGoogle_success() throws Exception {
        //given
        String redirectUrl = "redirectUrl";

        given(authService.loginOauthGoogle()).willReturn(
                new AuthUrlResponse(redirectUrl)
        );
        //when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/login/oauth/google"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.authUrl").value(redirectUrl))
                .andDo(restDocs.document());

    }

    @Test
    public void loginOauthGoogleCallback_success() throws Exception {
        //given
        TokenResponse tokenResponse = TokenResponse.of(
                "access_token",
                10,
                "refresh_token",
                10);

        given(authService.loginOauthGoogleCallback("code")).willReturn(
                tokenResponse
        );

        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/auth/login/oauth/google/callback")
                                .param("code", "code")
                )
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.cookie().value(
                        "access_token", tokenResponse.accessToken()))
                .andExpect(MockMvcResultMatchers.cookie().maxAge(
                        "access_token", tokenResponse.accessTokenMaxAge()))
                .andExpect(MockMvcResultMatchers.cookie().value(
                        "refresh_token", tokenResponse.refreshToken()))
                .andExpect(MockMvcResultMatchers.cookie().maxAge(
                        "refresh_token", tokenResponse.refreshTokenMaxAge()))
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**"))
                .andDo(restDocs.document());

    }


    @Test
    public void verifyAccessToken_success() throws Exception {
        //given
        AuthUserResponse authUserResponse = AuthUserResponse.of(
                1L);

        given(authService.verifyAccessToken("token")).willReturn(
                authUserResponse
        );
        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/auth/verify-access-token")
                                .cookie(new Cookie("access_token", "token"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value(authUserResponse.userId()))
                .andDo(restDocs.document());

    }

    @Test
    public void refreshAccessToken_success() throws Exception {
        //given
        TokenResponse tokenResponse = TokenResponse.of(
                "access_token",
                10,
                "refresh_token",
                10);

        given(authService.verifyRefreshToken("token")).willReturn(
                tokenResponse
        );

        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/refresh-access-token")
                                .cookie(new Cookie("refresh_token", "token"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().value(
                        "access_token", tokenResponse.accessToken()))
                .andExpect(MockMvcResultMatchers.cookie().maxAge(
                        "access_token", tokenResponse.accessTokenMaxAge()))
                .andExpect(MockMvcResultMatchers.cookie().value(
                        "refresh_token", tokenResponse.refreshToken()))
                .andExpect(MockMvcResultMatchers.cookie().maxAge(
                        "refresh_token", tokenResponse.refreshTokenMaxAge()))
                .andDo(restDocs.document());

    }

    @Test
    public void signOut_success() throws Exception {
        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/sign-out")
                                .cookie(new Cookie("refresh_token", "token"))
                                .cookie(new Cookie("access_token", "token"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().value(
                        "access_token", (String) null))
                .andExpect(MockMvcResultMatchers.cookie().maxAge(
                        "access_token", 0))
                .andExpect(MockMvcResultMatchers.cookie().value(
                        "refresh_token", (String) null))
                .andExpect(MockMvcResultMatchers.cookie().maxAge(
                        "refresh_token", 0))
                .andDo(restDocs.document());

    }

}