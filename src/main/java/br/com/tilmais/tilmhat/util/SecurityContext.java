package br.com.tilmais.tilmhat.util;

import br.com.tilmais.tilmhat.entity.TypeUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.NoSuchElementException;

public class SecurityContext {

    public static Long getIdFromAuthentication() throws NumberFormatException {
        String id = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return Long.parseLong(id);
    }

    public static TypeUser getAuthorityFromAuthentication() throws NoSuchElementException {
        return TypeUser.valueOf(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority()
        );
    }
}
