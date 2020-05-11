package br.com.tilmais.tilmhat.service.impl;

import br.com.tilmais.tilmhat.TilmhatApplicationTest;
import br.com.tilmais.tilmhat.entity.TypeUser;
import br.com.tilmais.tilmhat.entity.UserEntity;
import br.com.tilmais.tilmhat.repository.UserRepository;
import br.com.tilmais.tilmhat.util.BuilderUserEntityToTests;
import br.com.tilmais.tilmhat.util.BuilderUserRequestDtoToTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles(TilmhatApplicationTest.PROFILE_TEST)
class UserServiceTest {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    private void configurarSecurityContext(UserEntity userEntity) {
        final List<SimpleGrantedAuthority> list =
                Collections.singletonList(new SimpleGrantedAuthority(userEntity.getType().name()));
        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userEntity.getId(), null, list);
        final SecurityContextImpl context = new SecurityContextImpl(authenticationToken);
        SecurityContextHolder.setContext(context);
    }

    private UserEntity inserirUsuarioParaLoginNaBaseDeDados(UserEntity userEntity) {
        return this.userRepository.save(userEntity);
    }

    @AfterEach
    void exit() {
        this.userRepository.deleteAll();
    }

    @Test
    void shouldMakeAdministratorRegisterUserValidatingEncryptedPasswordAndReturnOptionalUriAndTypeMaster() {
        final UserEntity userEntity =
                this.inserirUsuarioParaLoginNaBaseDeDados(BuilderUserEntityToTests.getUserOfTypeAdministrator());
        this.configurarSecurityContext(userEntity);

        final Long idGenerateOfService = userEntity.getId() + 1;

        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .path(idGenerateOfService.toString())
                .build()
                .toUri();

        final Optional<URI> optionalURI = userService.registerUser(BuilderUserRequestDtoToTests.getUserOfTypeMaster());

        final Optional<UserEntity> optionalUserEntity = this.userRepository.findById(idGenerateOfService);

        final UserEntity userRegistered = optionalUserEntity.get();

        Assertions.assertNotNull(userRegistered.getPassword());
        Assertions.assertTrue(userRegistered.getPassword().length() >
                BuilderUserRequestDtoToTests.getUserOfTypeMaster().getPassword().length());
        Assertions.assertEquals(optionalURI, Optional.of(uri));
        Assertions.assertEquals(userRegistered.getType(), TypeUser.MASTER);
    }

    @Test
    void shouldMakeAdministratorRegisterUserOfTypeAdministratorValidatingAndReturnOptionalEmpty() {
        final UserEntity userEntity =
                this.inserirUsuarioParaLoginNaBaseDeDados(BuilderUserEntityToTests.getUserOfTypeAdministrator());
        this.configurarSecurityContext(userEntity);

        final Optional<URI> optionalURI =
                userService.registerUser(BuilderUserRequestDtoToTests.getUserOfTypeAdministrator());

        Assertions.assertEquals(optionalURI, Optional.empty());
    }

    @Test
    void shouldMakeAdministratorRegisterUserOfTypeCommonValidatingAndReturnOptionalEmpty() {
        final UserEntity userEntity =
                this.inserirUsuarioParaLoginNaBaseDeDados(BuilderUserEntityToTests.getUserOfTypeAdministrator());
        this.configurarSecurityContext(userEntity);

        final Optional<URI> optionalURI =
                userService.registerUser(BuilderUserRequestDtoToTests.getUserOfTypeCommon());

        Assertions.assertEquals(optionalURI, Optional.empty());
    }

    @Test
    void shouldMakeMasterRegisterUserValidatingEncryptedPasswordAndReturnOptionalUriAndTypeCommon() {
        final UserEntity userEntity =
                this.inserirUsuarioParaLoginNaBaseDeDados(BuilderUserEntityToTests.getUserOfTypeMaster());
        this.configurarSecurityContext(userEntity);

        final Long idGenerateOfService = userEntity.getId() + 1;

        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .path(idGenerateOfService.toString())
                .build()
                .toUri();

        final Optional<URI> optionalURI = userService.registerUser(BuilderUserRequestDtoToTests.getUserOfTypeCommon());

        final Optional<UserEntity> optionalUserEntity = this.userRepository.findById(idGenerateOfService);

        final UserEntity userRegistered = optionalUserEntity.get();

        Assertions.assertNotNull(userRegistered.getPassword());
        Assertions.assertTrue(userRegistered.getPassword().length() >
                BuilderUserRequestDtoToTests.getUserOfTypeMaster().getPassword().length());
        Assertions.assertEquals(optionalURI, Optional.of(uri));
        Assertions.assertEquals(userRegistered.getType(), TypeUser.COMMON);
    }

    @Test
    void shouldMakeMasterRegisterUserOfTypeMasterValidatingAndReturnOptionalEmpty() {
        final UserEntity userEntity =
                this.inserirUsuarioParaLoginNaBaseDeDados(BuilderUserEntityToTests.getUserOfTypeMaster());
        this.configurarSecurityContext(userEntity);

        final Optional<URI> optionalURI =
                userService.registerUser(BuilderUserRequestDtoToTests.getUserOfTypeMaster());

        Assertions.assertEquals(optionalURI, Optional.empty());
    }

    @Test
    void shouldMakeMasterRegisterUserOfTypeAdministratorValidatingAndReturnOptionalEmpty() {
        final UserEntity userEntity =
                this.inserirUsuarioParaLoginNaBaseDeDados(BuilderUserEntityToTests.getUserOfTypeMaster());
        this.configurarSecurityContext(userEntity);

        final Optional<URI> optionalURI =
                userService.registerUser(BuilderUserRequestDtoToTests.getUserOfTypeAdministrator());

        Assertions.assertEquals(optionalURI, Optional.empty());
    }

    @Test
    void shouldMakeCommonRegisterAnyUserAndReturnOptionalEmpty() {
        final UserEntity userEntity =
                this.inserirUsuarioParaLoginNaBaseDeDados(BuilderUserEntityToTests.getUserOfTypeCommon());
        this.configurarSecurityContext(userEntity);

        final Optional<URI> optionalURI = userService.registerUser(BuilderUserRequestDtoToTests.getUserOfTypeCommon());

        Assertions.assertEquals(optionalURI, Optional.empty());
    }
}
