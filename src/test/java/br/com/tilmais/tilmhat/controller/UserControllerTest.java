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
import java.util.Optional;


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

    private MockHttpServletRequestBuilder mountRequest(UserRequestDTO userRequestDTO, String token) throws Exception {
        return MockMvcRequestBuilders
                .post(UserController.PATH_PARENT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .content(objectMapper.writeValueAsString(userRequestDTO));
    }

    private LoginRequestDTO getAdministratorLoginRequestDTO() {
        final LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setLogin("administrator");
        loginRequestDTO.setPassword("123456789");
        return loginRequestDTO;
    }

    private LoginRequestDTO getCommonLoginRequestDTO() {
        final LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setLogin("common");
        loginRequestDTO.setPassword("123456789");
        return loginRequestDTO;
    }

    private LoginRequestDTO getMasterLoginRequestDTO() {
        final LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setLogin("master");
        loginRequestDTO.setPassword("123456789");
        return loginRequestDTO;
    }

    @BeforeEach
    void setup() {
        final UserEntity administrator = UserEntityBuilder.builder()
                .setName("administrator")
                .setPassword(BCrypt.hashpw("123456789", BCrypt.gensalt()))
                .setType(TypeUser.ADMINISTRATOR)
                .build();

        final UserEntity master = UserEntityBuilder.builder()
                .setName("master")
                .setPassword(BCrypt.hashpw("123456789", BCrypt.gensalt()))
                .setType(TypeUser.MASTER)
                .build();

        final UserEntity common = UserEntityBuilder.builder()
                .setName("common")
                .setPassword(BCrypt.hashpw("123456789", BCrypt.gensalt()))
                .setType(TypeUser.COMMON)
                .build();

        this.userRepository.save(administrator);
        this.userRepository.save(master);
        this.userRepository.save(common);
    }

    @AfterEach
    void exit() {
        this.userRepository.deleteAll();
    }

    @Test
    void shouldMakeRequestWithoutTokenAndGetResponseWithUnauthorizedStatus() throws Exception {
        final UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("master");
        userRequestDTO.setPassword("123456789");
        userRequestDTO.setType(TypeUser.MASTER);

        final ResultMatcher unauthorized = MockMvcResultMatchers.status().isUnauthorized();

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(UserController.PATH_PARENT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(userRequestDTO));

        this.mockMvc.perform(request).andExpect(unauthorized);
    }

    @Test
    void shouldMakeRequestAsAdministratorRegisterMasterAndReceiveResponseStatus201WithHeaderLocation() throws Exception {
        final UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("master");
        userRequestDTO.setPassword("123456789");
        userRequestDTO.setType(TypeUser.MASTER);

        final String token = this.makeRequestLoginAndGetToken(this.getAdministratorLoginRequestDTO());

        final ResultMatcher resultMatcherStatus = MockMvcResultMatchers
                .status()
                .isCreated();
        final ResultMatcher resultMatcherHeader = MockMvcResultMatchers
                .header()
                .stringValues(HttpHeaders.LOCATION, "http://localhost/users/1");

        final URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .path("users/1")
                .build()
                .toUri();

        Mockito.when(this.userService.registerUser(Mockito.any())).thenReturn(Optional.of(uri));

        this.mockMvc.perform(this.mountRequest(userRequestDTO, token))
                .andExpect(resultMatcherHeader)
                .andExpect(resultMatcherStatus);
    }

    @Test
    void shouldReceiveResponseStatus400() throws Exception {
        final String token = this.makeRequestLoginAndGetToken(this.getAdministratorLoginRequestDTO());

        this.mockMvc
                .perform(mountRequest(new UserRequestDTO(), token))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldMakeRequestAsCommonAndReturnStatusForbidden() throws Exception {
        final UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("common forbidden");
        userRequestDTO.setPassword("123456789");
        userRequestDTO.setType(TypeUser.COMMON);

        final String token = this.makeRequestLoginAndGetToken(this.getCommonLoginRequestDTO());

        final ResultMatcher resultMatcher = MockMvcResultMatchers.status().isForbidden();
        this.mockMvc.perform(this.mountRequest(userRequestDTO, token)).andExpect(resultMatcher);
    }

    @Test
    void shouldMakeRequestAsAdministratorRegisterAdministratorAndReturnStatusForbidden() throws Exception {
        final UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("administrator forbidden");
        userRequestDTO.setPassword("123456789");
        userRequestDTO.setType(TypeUser.ADMINISTRATOR);

        final String token = this.makeRequestLoginAndGetToken(this.getAdministratorLoginRequestDTO());

        final ResultMatcher resultMatcher = MockMvcResultMatchers.status().isForbidden();
        this.mockMvc.perform(this.mountRequest(userRequestDTO, token)).andExpect(resultMatcher);
    }

    @Test
    void shouldMakeRequestAsMasterRegisterMasterAndReturnStatusForbidden() throws Exception {
        final UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("master forbidden");
        userRequestDTO.setPassword("123456789");
        userRequestDTO.setType(TypeUser.MASTER);

        final String token = this.makeRequestLoginAndGetToken(this.getMasterLoginRequestDTO());

        final ResultMatcher resultMatcher = MockMvcResultMatchers.status().isForbidden();
        this.mockMvc.perform(this.mountRequest(userRequestDTO, token)).andExpect(resultMatcher);
    }

    @Test
    void shouldMakeRequestAsMasterRegisterCommonAndReceiveResponseStatus201WithHeaderLocation() throws Exception {
        final UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("common");
        userRequestDTO.setPassword("123456789");
        userRequestDTO.setType(TypeUser.COMMON);

        final String token = this.makeRequestLoginAndGetToken(this.getMasterLoginRequestDTO());

        final ResultMatcher resultMatcherStatus = MockMvcResultMatchers
                .status()
                .isCreated();
        final ResultMatcher resultMatcherHeader = MockMvcResultMatchers
                .header()
                .stringValues(HttpHeaders.LOCATION, "http://localhost/users/1");

        final URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .path("users/1")
                .build()
                .toUri();

        Mockito.when(this.userService.registerUser(Mockito.any())).thenReturn(Optional.of(uri));

        this.mockMvc.perform(this.mountRequest(userRequestDTO, token))
                .andExpect(resultMatcherHeader)
                .andExpect(resultMatcherStatus);
    }
}
