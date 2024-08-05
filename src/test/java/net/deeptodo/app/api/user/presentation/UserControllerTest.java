package net.deeptodo.app.api.user.presentation;

import jakarta.servlet.http.Cookie;
import net.deeptodo.app.aop.auth.AuthArgumentResolver;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.RestDocsIntegration;
import net.deeptodo.app.api.user.application.UserService;
import net.deeptodo.app.api.user.dto.request.UpdateUserProfileRequest;
import net.deeptodo.app.api.user.dto.response.GetUserProfileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Transactional
class UserControllerTest extends RestDocsIntegration {
    private final String URL_PATH = "/api/v1/account";

    @MockBean
    private UserService userService;

    @MockBean
    private AuthArgumentResolver authArgumentResolver;

    @BeforeEach
    public void beforeEach() throws Exception {
        AuthUserInfo authUserInfo = new AuthUserInfo(1L);
        given(authArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(authUserInfo);
    }

    @Test
    public void getUserProfile_success() throws Exception {
        //given
        GetUserProfileResponse response = GetUserProfileResponse.builder()
                .avatarUrl("https://avatarUrl")
                .email("email@email.com")
                .nickName("nickname")
                .build();

        given(userService.getUserProfile(any())).willReturn(response);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH + "/profile"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickName").value(response.nickName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.avatarUrl").value(response.avatarUrl()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(response.email()))
                .andDo(restDocs.document());
    }

    @Test
    public void updateUserProfile_success() throws Exception {
        //given
        UpdateUserProfileRequest request = UpdateUserProfileRequest.builder()
                .avatarUrl("https://changedUrl")
                .nickName("changedNickName")
                .build();

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.patch(URL_PATH + "/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(restDocs.document());
    }

    @Test
    public void deleteUser_success() throws Exception {
        //when & then
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH)
                        .cookie(new Cookie("refresh_token", "token"))
                        .cookie(new Cookie("access_token", "token"))
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
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