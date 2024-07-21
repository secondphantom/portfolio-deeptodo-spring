package net.deeptodo.app.controller;

import jakarta.servlet.http.Cookie;
import net.deeptodo.app.application.auth.AuthService;
import net.deeptodo.app.application.auth.dto.response.AuthUrlServiceResponse;
import net.deeptodo.app.application.auth.dto.response.AuthUserServiceResponse;
import net.deeptodo.app.application.auth.dto.response.TokenServiceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {


    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void loginOauthGoogle_success() throws Exception {
        //given
        String redirectUrl = "redirectUrl";

        given(authService.loginOauthGoogle()).willReturn(
                new AuthUrlServiceResponse(redirectUrl)
        );
        //when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/login/oauth/google"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl(redirectUrl));

    }

    @Test
    public void loginOauthGoogleCallback_success() throws Exception {
        //given
        TokenServiceResponse tokenServiceResponse = TokenServiceResponse.of(
                "access_token",
                10,
                "refresh_token",
                10);

        given(authService.loginOauthGoogleCallback("code")).willReturn(
                tokenServiceResponse
        );

        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/auth/login/oauth/google/callback")
                                .param("code", "code")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().value(
                        "access_token", tokenServiceResponse.accessToken()))
                .andExpect(MockMvcResultMatchers.cookie().maxAge(
                        "access_token", tokenServiceResponse.accessTokenMaxAge()))
                .andExpect(MockMvcResultMatchers.cookie().value(
                        "refresh_token", tokenServiceResponse.refreshToken()))
                .andExpect(MockMvcResultMatchers.cookie().maxAge(
                        "refresh_token", tokenServiceResponse.refreshTokenMaxAge()));
    }


    @Test
    public void verifyAccessToken_success() throws Exception {
        //given
        AuthUserServiceResponse authUserServiceResponse = AuthUserServiceResponse.of(
                1L);

        given(authService.verifyAccessToken("token")).willReturn(
                authUserServiceResponse
        );
        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/auth/verify-access-token")
                                .cookie(new Cookie("access_token", "token"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(authUserServiceResponse.userId()));
    }

    @Test
    public void refreshAccessToken_success() throws Exception {
        //given
        TokenServiceResponse tokenServiceResponse = TokenServiceResponse.of(
                "access_token",
                10,
                "refresh_token",
                10);

        given(authService.verifyRefreshToken("token")).willReturn(
                tokenServiceResponse
        );

        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/auth/refresh-access-token")
                                .cookie(new Cookie("refresh_token", "token"))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().value(
                        "access_token", tokenServiceResponse.accessToken()))
                .andExpect(MockMvcResultMatchers.cookie().maxAge(
                        "access_token", tokenServiceResponse.accessTokenMaxAge()))
                .andExpect(MockMvcResultMatchers.cookie().value(
                        "refresh_token", tokenServiceResponse.refreshToken()))
                .andExpect(MockMvcResultMatchers.cookie().maxAge(
                        "refresh_token", tokenServiceResponse.refreshTokenMaxAge()));
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
                        "refresh_token", 0));
    }

}