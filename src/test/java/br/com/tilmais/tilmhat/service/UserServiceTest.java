package br.com.tilmais.tilmhat.service;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.entity.TypeUser;
import br.com.tilmais.tilmhat.entity.UserEntity;
import br.com.tilmais.tilmhat.repository.UserRepository;
import br.com.tilmais.tilmhat.setting.ApplicationConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles(ApplicationConstants.PROFILE_TEST)
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    UserRequestDTO requestDTO;

    @Autowired
    UserService userService;

    @BeforeEach
    void setup() {
        this.requestDTO = new UserRequestDTO();
        this.requestDTO.setName("Register UserEntity");
//        this.requestDTO.setPassword("from one to nine");
        this.requestDTO.setPassword("123456789");
        this.requestDTO.setType(TypeUser.COMMON);
    }

    @Test
    void should_RegisterUser_And_ReturnUriPathWithId() {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .path("1")
                .build()
                .toUri();

        Assertions.assertEquals(this.userService.registerUser(this.requestDTO), uri);
    }

    @Test
    void should_RegisterUser_WithEncryptedPassword() {
        userService.registerUser(this.requestDTO);

        final Optional<UserEntity> optionalUserEntity = this.userRepository.findById(1L);

        if (optionalUserEntity.isPresent()) {
            System.out.println(optionalUserEntity.get().getPassword());
            Assertions.assertNotNull(optionalUserEntity.get().getPassword());
            Assertions.assertTrue(optionalUserEntity.get().getPassword().length() >
                    this.requestDTO.getPassword().length());
        }
    }
}
