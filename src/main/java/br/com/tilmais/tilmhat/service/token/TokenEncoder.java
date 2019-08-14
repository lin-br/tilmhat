package br.com.tilmais.tilmhat.service.token;

import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

public interface TokenEncoder {
    String getToken(String sub, LocalDateTime expirationDate, Collection<? extends GrantedAuthority> authorityList);
}
