package br.com.tilmais.tilmhat.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "users")
@DynamicInsert
@DynamicUpdate
public final class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    @ColumnDefault("'ACTIVED'")
    private SituationUser situation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 13)
    @ColumnDefault("'COMMON'")
    private TypeUser type;

    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    private LocalDateTime modified_at;

    private LocalDateTime deleted_at;

    UserEntity() {

    }

    UserEntity(Long id, String name, String password, SituationUser situation, TypeUser type, LocalDateTime created_at, LocalDateTime modified_at, LocalDateTime deleted_at) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.situation = situation;
        this.type = type;
        this.created_at = created_at;
        this.modified_at = modified_at;
        this.deleted_at = deleted_at;
    }

    public Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public SituationUser getSituation() {
        return situation;
    }

    void setSituation(SituationUser situation) {
        this.situation = situation;
    }

    public TypeUser getType() {
        return type;
    }

    void setType(TypeUser type) {
        this.type = type;
    }

    public Optional<LocalDateTime> getCreated_at() {
        return Optional.ofNullable(created_at);
    }

    void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Optional<LocalDateTime> getModified_at() {
        return Optional.ofNullable(modified_at);
    }

    void setModified_at(LocalDateTime modified_at) {
        this.modified_at = modified_at;
    }

    public Optional<LocalDateTime> getDeleted_at() {
        return Optional.ofNullable(deleted_at);
    }

    void setDeleted_at(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }

}
