package br.com.tilmais.tilmhat.controller;

import br.com.tilmais.tilmhat.TilmhatApplicationTest;
import br.com.tilmais.tilmhat.dto.LoginRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static br.com.tilmais.tilmhat.ApplicationSecurityConfig.AUTHENTICATION_PATH;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(TilmhatApplicationTest.PROFILE_TEST)
class AuthenticationControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetStatusUnauthorized() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setLogin("test");
        loginRequestDTO.setPassword("123456789");

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(AUTHENTICATION_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(loginRequestDTO));

        final ResultMatcher result = MockMvcResultMatchers.status().isUnauthorized();

        this.mockMvc.perform(request).andExpect(result);
    }

    @Test
    void shouldGetStatusMethodNotAllowed() throws Exception {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(AUTHENTICATION_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("");

        final ResultMatcher result = MockMvcResultMatchers.status().isMethodNotAllowed();

        this.mockMvc.perform(request).andExpect(result);
    }
}
