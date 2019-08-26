package br.com.tilmais.tilmhat.service;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.entity.TypeUser;
import br.com.tilmais.tilmhat.entity.UserEntity;
import br.com.tilmais.tilmhat.repository.UserRepository;
import br.com.tilmais.tilmhat.util.GeneratorURI;
import br.com.tilmais.tilmhat.util.SecurityContext;
import br.com.tilmais.tilmhat.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<URI> registerUser(UserRequestDTO dto) {
        final Long idLogin = SecurityContext.getIdFromAuthentication();
        final TypeUser authority = SecurityContext.getAuthorityFromAuthentication();

        final Optional<UserEntity> optionalUser = this.userRepository.findById(idLogin);

        if (optionalUser.isPresent()) {

            final UserEntity userEntity = optionalUser.get();

            if (authority.equals(TypeUser.ADMINISTRATOR) && dto.getType().equals(TypeUser.MASTER)) {
                return Optional.of(GeneratorURI.getUriFromCurrentRequestAddId(
                        this.userRepository.save(UserMapper.entityFromRequestDTO(dto, userEntity)).getId()));

            } else if (authority.equals(TypeUser.MASTER) && dto.getType().equals(TypeUser.COMMON)) {
                return Optional.of(GeneratorURI.getUriFromCurrentRequestAddId(
                        this.userRepository.save(UserMapper.entityFromRequestDTO(dto, userEntity)).getId()));
            }
        }
        return Optional.empty();
    }
}
