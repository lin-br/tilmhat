package br.com.tilmais.tilmhat.service.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface TokenDecoder {
    List<GrantedAuthority> getListGrantedAuthority();

    String getSubject();
}
