package br.com.tilmais.tilmhat.service.token.jwt;

import br.com.tilmais.tilmhat.service.token.TokenEncoder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtEncoder implements TokenEncoder {

    private final String nameOfListGrantedAuthority;
    private final String keySecret;

    public JwtEncoder(final String nameOfListGrantedAuthority,
                      final String keySecret) {
        this.nameOfListGrantedAuthority = nameOfListGrantedAuthority;
        this.keySecret = keySecret;
    }

    @Override
    public String getToken(final String sub,
                           final LocalDateTime expirationDate,
                           final Collection<? extends GrantedAuthority> authorityList) {
        Claims claims = Jwts
                .claims()
                .setIssuedAt(Date.from(Instant.now().atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant()))
                .setSubject(sub);

        claims.put(this.nameOfListGrantedAuthority,
                authorityList
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );

        JwtBuilder jwtBuilder = Jwts
                .builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, this.keySecret);

        return jwtBuilder.compact();
    }
}
