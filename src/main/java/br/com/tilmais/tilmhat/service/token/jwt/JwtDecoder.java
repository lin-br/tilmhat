package br.com.tilmais.tilmhat.service.token.jwt;

import br.com.tilmais.tilmhat.service.token.TokenDecoder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JwtDecoder implements TokenDecoder {

    private final String nameOfListGrantedAuthority;
    private final Claims claims;

    public JwtDecoder(final String jwt,
                      final String keySecret,
                      final String nameOfListGrantedAuthority) {
        this.claims = Jwts.parser().setSigningKey(keySecret).parseClaimsJws(jwt).getBody();
        this.nameOfListGrantedAuthority = nameOfListGrantedAuthority;
    }

    @Override
    public List<GrantedAuthority> getListGrantedAuthority() {
        if (this.claims.containsKey(this.nameOfListGrantedAuthority)) {
            final List<String> list = this.claims.get(this.nameOfListGrantedAuthority, List.class);

            return list.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    public String getSubject() {
        return this.claims.getSubject();
    }
}
