package br.com.tilmais.tilmhat.entity;

import java.time.LocalDateTime;

public class UserEntityBuilder {

    private Long id;
    private String name;
    private String password;
    private SituationUser situation;
    private TypeUser type;
    private LocalDateTime created_at;
    private LocalDateTime modified_at;
    private LocalDateTime deleted_at;

    private UserEntityBuilder() {
    }

    public static UserEntityBuilder builder() {
        return new UserEntityBuilder();
    }

    public UserEntityBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public UserEntityBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserEntityBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserEntityBuilder setSituation(SituationUser situation) {
        this.situation = situation;
        return this;
    }

    public UserEntityBuilder setType(TypeUser type) {
        this.type = type;
        return this;
    }

    public UserEntityBuilder setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
        return this;
    }

    public UserEntityBuilder setModified_at(LocalDateTime modified_at) {
        this.modified_at = modified_at;
        return this;
    }

    public UserEntityBuilder setDeleted_at(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
        return this;
    }

    public UserEntity build() {
        return new UserEntity(
                this.id,
                this.name,
                this.password,
                this.situation,
                this.type,
                this.created_at,
                this.modified_at,
                this.deleted_at
        );
    }
}
