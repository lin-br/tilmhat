package br.com.tilmais.tilmhat.util;

import br.com.tilmais.tilmhat.entity.TypeUser;
import br.com.tilmais.tilmhat.entity.UserEntity;
import br.com.tilmais.tilmhat.entity.UserEntityBuilder;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class BuilderUserEntityToTests {

    public static UserEntity getUserOfTypeAdministrator() {
        return UserEntityBuilder.builder()
                .setName(ConstantsToTests.NAME_USER_ADMINISTRATOR_REQUEST_DTO)
                .setPassword(BCrypt.hashpw("123456789", BCrypt.gensalt()))
                .setType(TypeUser.ADMINISTRATOR)
                .build();
    }

    public static UserEntity getUserOfTypeMaster() {
        return UserEntityBuilder.builder()
                .setName(ConstantsToTests.NAME_USER_MASTER_REQUEST_DTO)
                .setPassword(BCrypt.hashpw("123456789", BCrypt.gensalt()))
                .setType(TypeUser.MASTER)
                .build();
    }

    public static UserEntity getUserOfTypeCommon() {
        return UserEntityBuilder.builder()
                .setName(ConstantsToTests.NAME_USER_COMMON_REQUEST_DTO)
                .setPassword(BCrypt.hashpw("123456789", BCrypt.gensalt()))
                .setType(TypeUser.COMMON)
                .build();
    }
}
