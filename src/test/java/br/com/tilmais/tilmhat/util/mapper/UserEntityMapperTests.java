package br.com.tilmais.tilmhat.util.mapper;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.entity.TypeUser;
import br.com.tilmais.tilmhat.entity.UserEntity;
import br.com.tilmais.tilmhat.entity.UserEntityBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Random;

class UserEntityMapperTests {

    private UserRequestDTO requestDTO;

    private UserEntity parent;

    @BeforeEach
    void setup() {
        this.requestDTO = new UserRequestDTO();
        this.requestDTO.setName("Dom Cas Zarabatana");
        this.requestDTO.setPassword("from one to nine");
        this.requestDTO.setType(TypeUser.MASTER);

        this.parent = UserEntityBuilder.builder()
                .setId(new Random(1).nextLong())
                .setName("Parent of Test")
                .setPassword("000")
                .setType(TypeUser.ADMINISTRATOR)
                .build();
    }

    @Test
    void should_Return_MasterTypeUser() {
        UserEntity userEntity = UserMapper.entityFromRequestDTO(this.requestDTO, parent);
        Assertions.assertThat(userEntity.getType()).isEqualByComparingTo(TypeUser.MASTER);
    }

    @Test
    void should_ReturnUser_WithEmptyDateFields() {
        UserEntity userEntity = UserMapper.entityFromRequestDTO(this.requestDTO, parent);
        Assertions.assertThat(userEntity.getCreated_at()).isEqualTo(Optional.empty());
        Assertions.assertThat(userEntity.getModified_at()).isEqualTo(Optional.empty());
        Assertions.assertThat(userEntity.getDeleted_at()).isEqualTo(Optional.empty());
    }

    @Test
    void should_ReturnUser_WithParentNotNull() {
        UserEntity userEntity = UserMapper.entityFromRequestDTO(this.requestDTO, parent);
        Assertions.assertThat(userEntity).isNotNull();
    }
}
