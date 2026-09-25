package com.SpringBootMVC.ExpensesTracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GoogleLoginFlowTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginPageShouldExposeGoogleOAuthLink() throws Exception {
        mockMvc.perform(get("/showLoginPage"))
            .andExpect(status().isOk())
            .andExpect(result -> assertTrue(
                result.getResponse().getContentAsString().contains("/oauth2/authorization/google")));
    }
}
