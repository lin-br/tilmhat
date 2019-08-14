package br.com.tilmais.tilmhat.setting.security;

import br.com.tilmais.tilmhat.dto.LoginRequestDTO;
import br.com.tilmais.tilmhat.service.token.TokenEncoder;
import br.com.tilmais.tilmhat.service.token.TokenFactory;
import br.com.tilmais.tilmhat.setting.ApplicationConstants;
import br.com.tilmais.tilmhat.setting.ApplicationProperties;
import br.com.tilmais.tilmhat.util.UnauthorizedUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private ApplicationProperties applicationProperties;

    public LoginFilter(AuthenticationManager authenticationManager, ApplicationProperties applicationProperties) {
        super(new AntPathRequestMatcher(ApplicationConstants.AUTHENTICATION_PATH, HttpMethod.POST.name()));
        this.setAuthenticationManager(authenticationManager);
        this.applicationProperties = applicationProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException {

        LoginRequestDTO loginRequestDTO = new ObjectMapper()
                .readValue(request.getInputStream(), LoginRequestDTO.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getLogin(),
                loginRequestDTO.getPassword()
        );

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader(UnauthorizedUtil.WWWAuthenticate, UnauthorizedUtil.getWWWAuthenticateMessage(request, failed));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {


        final TokenEncoder encoder = TokenFactory.getEncoder(
                this.applicationProperties.getToken().getClaimsName(),
                this.applicationProperties.getToken().getKey()
        );

        final Duration duration = Duration
                .ofMillis(this.applicationProperties.getToken().getDurationInMilliseconds());

        final LocalDateTime exp = LocalDateTime
                .now()
                .atZone(ZoneId.systemDefault())
                .plus(duration)
                .toLocalDateTime();

        final String jwt = encoder.getToken(authResult.getName(), exp, authResult.getAuthorities());

        response.addHeader(
                this.applicationProperties.getToken().getHeader(),
                this.applicationProperties.getToken().getPrefix() + " " + jwt
        );
    }
}
