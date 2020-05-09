package br.com.tilmais.tilmhat.service.impl;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.entity.TypeUser;
import br.com.tilmais.tilmhat.entity.UserEntity;
import br.com.tilmais.tilmhat.repository.UserRepository;
import br.com.tilmais.tilmhat.service.UserService;
import br.com.tilmais.tilmhat.util.GeneratorURI;
import br.com.tilmais.tilmhat.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;

@Service
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<URI> registerUser(final UserRequestDTO dto) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final Long id = Long.valueOf(String.valueOf(authentication.getPrincipal()));

        return userRepository.findById(id)
                .filter(userEntity1 -> isValidUserType(userEntity1, dto))
                .map(userEntity -> userRepository.save(UserMapper.entityFromRequestDTO(dto, userEntity)))
                .map(userEntity -> GeneratorURI.getUriFromCurrentRequestAddId(userEntity.getId()));
    }

    private boolean isValidUserType(final UserEntity userEntity, final UserRequestDTO dto) {
        boolean administrator = userEntity.getType().equals(TypeUser.ADMINISTRATOR) &&
                dto.getType().equals(TypeUser.MASTER);
        boolean master = userEntity.getType().equals(TypeUser.MASTER) &&
                dto.getType().equals(TypeUser.COMMON);
        return administrator || master;
    }
}
