package net.deeptodo.app.aop.dto.response;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ResponseWrapperAspectTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void wrapResponse() throws Exception {
        //when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/plans")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andReturn();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").exists())
                .andReturn();
    }
}