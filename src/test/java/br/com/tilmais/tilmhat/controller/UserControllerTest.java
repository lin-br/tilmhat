package br.com.tilmais.tilmhat.controller;

import br.com.tilmais.tilmhat.dto.LoginRequestDTO;
import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.entity.TypeUser;
import br.com.tilmais.tilmhat.entity.UserEntity;
import br.com.tilmais.tilmhat.entity.UserEntityBuilder;
import br.com.tilmais.tilmhat.repository.UserRepository;
import br.com.tilmais.tilmhat.service.UserService;
import br.com.tilmais.tilmhat.setting.ApplicationConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(ApplicationConstants.PROFILE_TEST)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private String token;
    private UserRequestDTO userRequestDTO;

    private String makeRequestLoginAndGetToken(LoginRequestDTO loginRequestDTO) throws Exception {
        final MockHttpServletRequestBuilder mockRequestLogin = MockMvcRequestBuilders
                .post(ApplicationConstants.AUTHENTICATION_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(loginRequestDTO));

        return this.mockMvc
                .perform(mockRequestLogin)
                .andReturn()
                .getResponse()
                .getHeader(HttpHeaders.AUTHORIZATION);
    }

    private MockHttpServletRequestBuilder mountRequest(UserRequestDTO userRequestDTO) throws Exception {
        return MockMvcRequestBuilders
                .post(UserController.PATH_PARENT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, this.token)
                .content(objectMapper.writeValueAsString(userRequestDTO));
    }

    @BeforeEach
    void setup() throws Exception {
        this.userRequestDTO = new UserRequestDTO();
        this.userRequestDTO.setName("Testing user controller");
        this.userRequestDTO.setPassword("123456789");
        this.userRequestDTO.setType(TypeUser.ADMINISTRATOR);

        final UserEntity test = UserEntityBuilder.builder()
                .setName("test")
                .setPassword(BCrypt.hashpw("123456789", BCrypt.gensalt()))
                .build();
        this.userRepository.save(test);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setLogin("test");
        loginRequestDTO.setPassword("123456789");

        this.token = this.makeRequestLoginAndGetToken(loginRequestDTO);
    }

    @AfterEach
    void exit() {
        this.userRepository.deleteAll();
    }

    @Test
    void should_MakeRequestWithoutToken_And_GetResponseWith_UnauthorizedStatus() throws Exception {
        final ResultMatcher unauthorized = MockMvcResultMatchers.status().isUnauthorized();

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(UserController.PATH_PARENT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(this.userRequestDTO));

        this.mockMvc.perform(request).andExpect(unauthorized);
    }

    @Test
    void should_RegisterUser_And_ReceiveResponseStatus201() throws Exception {
        ResultMatcher resultMatcher = MockMvcResultMatchers.status().isCreated();
        this.mockMvc.perform(this.mountRequest(this.userRequestDTO)).andExpect(resultMatcher);
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

        this.mockMvc.perform(mountRequest(this.userRequestDTO)).andExpect(resultMatcher);
    }

    @Test
    void should_ReceiveResponseStatus400() throws Exception {
        this.mockMvc
                .perform(mountRequest(new UserRequestDTO()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
