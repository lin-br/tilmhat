package br.com.tilmais.tilmhat.util.mapper;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.entity.TypeUser;
import br.com.tilmais.tilmhat.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class UserEntityMapperTests {

    private UserRequestDTO requestDTO;

    @BeforeEach
    void setup() {
        this.requestDTO = new UserRequestDTO();
        this.requestDTO.setName("Dom Cas Zarabatana");
        this.requestDTO.setPassword("from one to nine");
        this.requestDTO.setType(TypeUser.MASTER);
    }

    @Test
    void should_Return_MasterTypeUser() {
        UserEntity userEntity = UserMapper.entityFromRequestDTO(this.requestDTO);
        Assertions.assertThat(userEntity.getType()).isEqualByComparingTo(TypeUser.MASTER);
    }

    @Test
    void should_ReturnUser_WithEmptyDateFields() {
        UserEntity userEntity = UserMapper.entityFromRequestDTO(this.requestDTO);
        Assertions.assertThat(userEntity.getCreated_at()).isEqualTo(Optional.empty());
        Assertions.assertThat(userEntity.getModified_at()).isEqualTo(Optional.empty());
        Assertions.assertThat(userEntity.getDeleted_at()).isEqualTo(Optional.empty());
    }
}
