package br.com.tilmais.tilmhat.service;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;

import java.net.URI;
import java.util.Optional;

public interface UserService {
    Optional<URI> registerUser(UserRequestDTO dto);
}
