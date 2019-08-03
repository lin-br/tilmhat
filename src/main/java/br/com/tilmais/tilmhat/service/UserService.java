package br.com.tilmais.tilmhat.service;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.repository.UserRepository;
import br.com.tilmais.tilmhat.util.GeneratorURI;
import br.com.tilmais.tilmhat.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public URI registerUser(UserRequestDTO dto) {
        return GeneratorURI.getUriFromCurrentRequestAddId(
                this.userRepository.save(UserMapper.entityFromRequestDTO(dto)).getId());
    }
}
