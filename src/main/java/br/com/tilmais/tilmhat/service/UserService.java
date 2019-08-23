package br.com.tilmais.tilmhat.service;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.entity.TypeUser;
import br.com.tilmais.tilmhat.entity.UserEntity;
import br.com.tilmais.tilmhat.repository.UserRepository;
import br.com.tilmais.tilmhat.util.GeneratorURI;
import br.com.tilmais.tilmhat.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String id = String.valueOf(authentication.getPrincipal());

        final Optional<UserEntity> optionalUser = this.userRepository.findById(Long.parseLong(id));

        if (optionalUser.isPresent()) {

            final UserEntity userEntity = optionalUser.get();

            if (userEntity.getType().equals(TypeUser.ADMINISTRATOR) && dto.getType().equals(TypeUser.MASTER)) {
                return Optional.of(GeneratorURI.getUriFromCurrentRequestAddId(
                        this.userRepository.save(UserMapper.entityFromRequestDTO(dto, userEntity)).getId()));

            } else if (userEntity.getType().equals(TypeUser.MASTER) && dto.getType().equals(TypeUser.COMMON)) {
                return Optional.of(GeneratorURI.getUriFromCurrentRequestAddId(
                        this.userRepository.save(UserMapper.entityFromRequestDTO(dto, userEntity)).getId()));
            }
        }
        return Optional.empty();
    }
}
