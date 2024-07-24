package net.deeptodo.app.aop.auth;

import jakarta.servlet.http.Cookie;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.auth.application.AuthService;
import net.deeptodo.app.api.auth.dto.response.AuthUserResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
class AuthInterceptorTest {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthInterceptor authInterceptor;

    @Test
    public void preHandle_success() throws Exception {
        //given
        AuthUserResponse authUserResponse = AuthUserResponse.of(1L);
        given(authService.verifyAccessToken("token")).willReturn(
                authUserResponse
        );
        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/projects")
                                .cookie(new Cookie("access_token", "token")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void preHandle_success_authUser() throws Exception {

        // Given
        String token = "valid-token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setCookies(new Cookie("access_token", token));

        AuthUserResponse authUserResponse = AuthUserResponse.of(1L);
        given(authService.verifyAccessToken(token)).willReturn(authUserResponse);

        // When
        boolean result = authInterceptor.preHandle(request, response, new Object());

        // Then
        Assertions.assertThat(result).isEqualTo(true);
        AuthUserInfo authUser = (AuthUserInfo) request.getAttribute("authUserInfo");
        Assertions.assertThat(authUser.userId()).isEqualTo(authUserResponse.userId());
    }

    @Test
    public void preHandle_fail_token_is_empty() throws Exception {
        //given
        AuthUserResponse authUserResponse = AuthUserResponse.of(1L);
        given(authService.verifyAccessToken("token")).willReturn(
                authUserResponse
        );
        //when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/projects"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


}