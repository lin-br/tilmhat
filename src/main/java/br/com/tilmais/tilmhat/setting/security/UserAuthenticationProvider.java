package br.com.tilmais.tilmhat.setting.security;

import br.com.tilmais.tilmhat.entity.UserEntity;
import br.com.tilmais.tilmhat.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);

    private final UserRepository repository;

    @Autowired
    UserAuthenticationProvider(final UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

        if (authentication.isAuthenticated()) return authentication;

        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();

        String message = String.format("Authentication requested with the username = %s and password = %s", username, password);
        logger.info(message);

        return this.repository.findByName(username)
                .filter(user -> BCrypt.checkpw(password, user.getPassword()))
                .map(this::getAuthObject)
                .orElseThrow(() -> new BadCredentialsException("Username or password not found"));
    }

    private UsernamePasswordAuthenticationToken getAuthObject(final UserEntity user) {
        return new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getType().name())));
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
