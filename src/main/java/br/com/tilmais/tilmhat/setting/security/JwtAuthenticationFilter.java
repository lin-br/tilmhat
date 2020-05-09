package br.com.tilmais.tilmhat.setting.security;

import br.com.tilmais.tilmhat.service.token.TokenDecoder;
import br.com.tilmais.tilmhat.service.token.TokenFactory;
import br.com.tilmais.tilmhat.setting.ApplicationProperties;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ApplicationProperties applicationProperties;

    @Autowired
    JwtAuthenticationFilter(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    private String getToken(HttpServletRequest request) throws JwtException {
        final String header = request.getHeader(this.applicationProperties.getToken().getHeader());

        if (header == null || header.isEmpty()) throw new JwtException("Token in authentication header not found");

        return header.replace(this.applicationProperties.getToken().getPrefix(), "").trim();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException, JwtException {

        final String token = this.getToken(request);

        final TokenDecoder decoder = TokenFactory.getDecoder(
                token,
                this.applicationProperties.getToken().getKey(),
                this.applicationProperties.getToken().getClaimsName());

        final String subJwt = decoder.getSubject();

        final List<GrantedAuthority> listGrantedAuthority = decoder.getListGrantedAuthority();

        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(subJwt, null, listGrantedAuthority);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
