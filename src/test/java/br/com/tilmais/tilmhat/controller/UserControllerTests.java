package br.com.tilmais.tilmhat.controller;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.entity.TypeUser;
import br.com.tilmais.tilmhat.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@WebMvcTest(controllers = UserController.class)
class UserControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserRequestDTO requestDTO;

    private MockHttpServletRequestBuilder mountRequest(UserRequestDTO userRequestDTO) throws Exception {
        return MockMvcRequestBuilders
                .post(UserController.PATH_PARENT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userRequestDTO));
    }

    @BeforeEach
    void setup() {
        this.requestDTO = new UserRequestDTO();
        this.requestDTO.setName("São João da Terra Nova");
        this.requestDTO.setPassword("123456789");
        this.requestDTO.setType(TypeUser.ADMINISTRATOR);
    }

    @Test
    void should_RegisterUser_And_ReceiveResponseStatus201() throws Exception {
        ResultMatcher resultMatcher = MockMvcResultMatchers
                .status()
                .isCreated();

        this.mockMvc.perform(mountRequest(this.requestDTO)).andExpect(resultMatcher);
    }

    @Test
    void should_RegisterUser_And_ReceiveHeaderLocationWithId() throws Exception {
        final URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .path("users/1")
                .build()
                .toUri();

        final ResultMatcher resultMatcher = MockMvcResultMatchers
                .header()
                .stringValues(HttpHeaders.LOCATION, "http://localhost/users/1");

        Mockito.when(this.userService.registerUser(Mockito.any())).thenReturn(uri);

        this.mockMvc.perform(mountRequest(this.requestDTO)).andExpect(resultMatcher);
    }

    @Test
    void should_ReceiveResponseStatus400() throws Exception {
        this.mockMvc
                .perform(mountRequest(new UserRequestDTO()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
