package br.com.tilmais.tilmhat.repository;

import br.com.tilmais.tilmhat.setting.ApplicationConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles(ApplicationConstants.PROFILE_TEST)
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void injectedComponentAreNotNull() {
        Assertions.assertThat(repository).isNotNull();
    }

    @Test
    @Sql(statements = "INSERT INTO users (id, name, password) VALUES (1, 'Joana', '123456789')")
    void whenInitialized_thenFindById() {
        Assertions.assertThat(this.repository.findById(1L).get().getName()).isEqualTo("Joana");
    }

    @Test
    @Sql(statements = "INSERT INTO users (id, name, password) VALUES (1, 'Test find user by name', 'from one to nine')")
    void should_FindUserByName() {
        Assertions.assertThat(this.repository.findByName("Test find user by name").get()
                .getPassword()).isEqualTo("from one to nine");
    }
}
