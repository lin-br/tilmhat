package br.com.tilmais.tilmhat.service;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.entity.TypeUser;
import br.com.tilmais.tilmhat.entity.UserEntity;
import br.com.tilmais.tilmhat.entity.UserEntityBuilder;
import br.com.tilmais.tilmhat.repository.UserRepository;
import br.com.tilmais.tilmhat.setting.ApplicationConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@SpringBootTest
@ActiveProfiles(ApplicationConstants.PROFILE_TEST)
class UserServiceTests {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    void should_RegisterUser_And_ReturnUriPathWithId() {
        UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName("Register UserEntity");
        requestDTO.setPassword("from one to nine");
        requestDTO.setType(TypeUser.COMMON);

        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .path("1")
                .build()
                .toUri();

        UserEntity userEntity = UserEntityBuilder.builder().setId(1L).build();

        Mockito.when(this.userRepository.save(Mockito.any()))
                .thenReturn(userEntity);

        Assertions.assertEquals(this.userService.registerUser(requestDTO), uri);
    }
}
