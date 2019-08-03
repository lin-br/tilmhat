package br.com.tilmais.tilmhat.dto;

import br.com.tilmais.tilmhat.entity.TypeUser;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRequestDTO {

    @NotNull
    @Size(min = 5, max = 45)
    private String name;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @NotNull
    private TypeUser type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TypeUser getType() {
        return type;
    }

    public void setType(TypeUser type) {
        this.type = type;
    }
}
