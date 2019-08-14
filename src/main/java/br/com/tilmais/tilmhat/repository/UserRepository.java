package br.com.tilmais.tilmhat.repository;

import br.com.tilmais.tilmhat.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByName(String name);
}
