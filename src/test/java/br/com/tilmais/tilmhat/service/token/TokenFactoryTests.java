package br.com.tilmais.tilmhat.service.token;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TokenFactoryTests {

    private static final String KEY_SECRET = "keysecret top the test";
    private static final String NAME_OF_LIST_GRANTED_AUTHORITY = "test";
    private TokenEncoder encoder;

    @BeforeEach
    void setup() {
        this.encoder = TokenFactory.getEncoder(NAME_OF_LIST_GRANTED_AUTHORITY, KEY_SECRET);
    }

    @Test
    void should_GetSameSubject_AsTheEncoder() {
        final String subject = "this is a test";

        final String token = this.encoder.getToken(subject,
                LocalDateTime.now().plus(1, ChronoUnit.DAYS),
                Arrays.asList(new SimpleGrantedAuthority("test")));

        final TokenDecoder decoder =
                TokenFactory.getDecoder(token, KEY_SECRET, NAME_OF_LIST_GRANTED_AUTHORITY);

        Assertions.assertEquals(decoder.getSubject(), subject);
    }

    @Test
    void should_GetToken_WithThreeParts() {
        final String token = this.encoder.getToken("testWithThreeParts",
                LocalDateTime.now().plus(1, ChronoUnit.DAYS),
                Arrays.asList(new SimpleGrantedAuthority("role in test")));

        final Matcher matcher = Pattern.compile("(\\S+)[.](\\S+)[.](\\S+)").matcher(token);

        if (matcher.find()) {
            Assertions.assertEquals(3, matcher.groupCount());
        }
    }

    @Test
    void should_GetEmpty_ListGrantedAuthority() {
        final String token = this.encoder.getToken("testEmptyListGrantedAuthority",
                LocalDateTime.now().plus(1, ChronoUnit.DAYS),
                Arrays.asList(new SimpleGrantedAuthority("role in test")));

        final List<GrantedAuthority> listGrantedAuthority =
                TokenFactory.getDecoder(token, KEY_SECRET, "")
                        .getListGrantedAuthority();

        Assertions.assertTrue(listGrantedAuthority.isEmpty());
    }
}
