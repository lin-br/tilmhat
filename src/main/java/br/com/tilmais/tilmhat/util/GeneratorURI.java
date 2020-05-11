package br.com.tilmais.tilmhat.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class GeneratorURI {
    private GeneratorURI() {
    }

    public static URI getUriFromCurrentRequestAddId(final Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
