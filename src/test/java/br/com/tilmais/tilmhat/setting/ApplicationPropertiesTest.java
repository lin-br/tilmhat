package br.com.tilmais.tilmhat.setting;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ApplicationProperties.class)
class ApplicationPropertiesTest {

    @Autowired
    ApplicationProperties applicationProperties;

    @Test
    void should_Populate_ApplicationProperties() {
        Assertions.assertThat(this.applicationProperties.getName()).isNotNull();
        Assertions.assertThat(this.applicationProperties.getVersion()).isNotNull();
        Assertions.assertThat(this.applicationProperties.getToken().getKey()).isNotNull();
    }
}
