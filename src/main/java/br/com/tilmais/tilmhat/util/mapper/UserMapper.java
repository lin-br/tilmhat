package br.com.tilmais.tilmhat.util.mapper;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.entity.UserEntity;
import br.com.tilmais.tilmhat.entity.UserEntityBuilder;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class UserMapper {

    public static UserEntity entityFromRequestDTO(UserRequestDTO dto, UserEntity parent) {
        return UserEntityBuilder.builder()
                .setName(dto.getName())
                .setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()))
                .setType(dto.getType())
                .setParent(parent)
                .build();
    }
}
