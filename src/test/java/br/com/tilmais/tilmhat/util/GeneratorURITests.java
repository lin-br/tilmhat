package br.com.tilmais.tilmhat.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;

class GeneratorURITests {

    @BeforeEach
    void setup() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));
    }

    @Test
    void should_CreateURI_WithPathEquals1() {
        final URI uri = GeneratorURI.getUriFromCurrentRequestAddId(1L);
        Assertions.assertThat(uri.getPath()).isEqualTo("/1");
    }
}