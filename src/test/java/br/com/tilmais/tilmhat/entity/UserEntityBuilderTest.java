package br.com.tilmais.tilmhat.entity;

import br.com.tilmais.tilmhat.util.ManipulateClassWithReflection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

class UserEntityBuilderTest {

    @Test
    void shouldFillTheEntireEntity() {
        final UserEntity parent = UserEntityBuilder.builder().build();

        final UserEntity userEntity = UserEntityBuilder.builder()
                .setId(1L)
                .setName("Test Builder")
                .setPassword("123456789")
                .setType(TypeUser.ADMINISTRATOR)
                .setSituation(SituationUser.ACTIVED)
                .setCreated_at(LocalDateTime.now())
                .setModified_at(LocalDateTime.now())
                .setDeleted_at(LocalDateTime.now())
                .setParent(parent)
                .build();

        final List<Method> allGettersPublic =
                ManipulateClassWithReflection.getAllGettersPublic(UserEntity.class);

        final List<Object> allValuesFromMethods =
                ManipulateClassWithReflection.getAllValuesFromMethods(allGettersPublic, userEntity);

        allValuesFromMethods.forEach(Assertions::assertNotNull);
    }
}
