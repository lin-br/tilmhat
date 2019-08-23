package br.com.tilmais.tilmhat.util;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.entity.TypeUser;

public class BuilderUserRequestDtoToTests {

    public static UserRequestDTO getUserOfTypeAdministrator() {
        final UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName(ConstantsToTests.NAME_USER_ADMINISTRATOR_REQUEST_DTO);
        requestDTO.setPassword("123456789");
        requestDTO.setType(TypeUser.ADMINISTRATOR);
        return requestDTO;
    }

    public static UserRequestDTO getUserOfTypeMaster() {
        final UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName(ConstantsToTests.NAME_USER_MASTER_REQUEST_DTO);
        requestDTO.setPassword("123456789");
        requestDTO.setType(TypeUser.MASTER);
        return requestDTO;
    }

    public static UserRequestDTO getUserOfTypeCommon() {
        final UserRequestDTO requestDTO = new UserRequestDTO();
        requestDTO.setName(ConstantsToTests.NAME_USER_COMMON_REQUEST_DTO);
        requestDTO.setPassword("123456789");
        requestDTO.setType(TypeUser.COMMON);
        return requestDTO;
    }
}
