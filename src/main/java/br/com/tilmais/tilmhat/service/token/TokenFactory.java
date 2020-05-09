package br.com.tilmais.tilmhat.service.token;

import br.com.tilmais.tilmhat.service.token.jwt.JwtDecoder;
import br.com.tilmais.tilmhat.service.token.jwt.JwtEncoder;

public class TokenFactory {

    private TokenFactory() {
    }

    public static TokenEncoder getEncoder(final String nameOfListGrantedAuthority,
                                          final String keySecret) {
        return new JwtEncoder(nameOfListGrantedAuthority, keySecret);
    }

    public static TokenDecoder getDecoder(final String jwt,
                                          final String keySecret,
                                          final String nameOfListGrantedAuthority) {
        return new JwtDecoder(jwt, keySecret, nameOfListGrantedAuthority);
    }
}
