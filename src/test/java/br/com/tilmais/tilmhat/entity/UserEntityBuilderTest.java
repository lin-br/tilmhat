package br.com.tilmais.tilmhat.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class UserEntityBuilderTest {

    @Test
    void shouldFillTheEntireEntity() {
        final UserEntity userEntity = UserEntityBuilder.builder()
                .setId(1L)
                .setName("Test Builder")
                .setPassword("123456789")
                .setType(TypeUser.ADMINISTRATOR)
                .setSituation(SituationUser.ACTIVED)
                .setCreated_at(LocalDateTime.now())
                .setModified_at(LocalDateTime.now())
                .setDeleted_at(LocalDateTime.now())
                .build();

        final Method[] declaredMethods = UserEntity.class.getDeclaredMethods();
        final List<Method> methods = Arrays.asList(declaredMethods);

        methods.stream().filter(method -> method.getModifiers() == Modifier.PUBLIC).forEach(method -> {
            try {
                final Object object = method.invoke(userEntity);

                if (object instanceof Optional) Assertions.assertTrue(((Optional) object).isPresent());
                Assertions.assertNotNull(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
