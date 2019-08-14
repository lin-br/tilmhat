package br.com.tilmais.tilmhat.service.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JwtDecoder implements TokenDecoder {

    private String nameOfListGrantedAuthority;
    private Claims claims;

    JwtDecoder(String jwt, String keySecret, String nameOfListGrantedAuthority) {
        this.claims = Jwts.parser().setSigningKey(keySecret).parseClaimsJws(jwt).getBody();
        this.nameOfListGrantedAuthority = nameOfListGrantedAuthority;
    }

    @Override
    public List<GrantedAuthority> getListGrantedAuthority() {
        if (this.claims.containsKey(this.nameOfListGrantedAuthority)) {
            final List<String> list = this.claims.get(this.nameOfListGrantedAuthority, List.class);

            return list.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    @Override
    public String getSubject() {
        return this.claims.getSubject();
    }
}
