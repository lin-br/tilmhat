package br.com.tilmais.tilmhat.util.mapper;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.entity.UserEntity;
import br.com.tilmais.tilmhat.entity.UserEntityBuilder;

public class UserMapper {

    public static UserEntity entityFromRequestDTO(UserRequestDTO dto) {
        return UserEntityBuilder.builder()
                .setName(dto.getName())
                .setPassword(dto.getPassword())
                .setType(dto.getType())
                .build();
    }
}
