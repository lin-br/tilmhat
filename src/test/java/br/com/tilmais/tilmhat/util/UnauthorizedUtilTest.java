package br.com.tilmais.tilmhat.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class UnauthorizedUtilTest {

    private Exception exception = new Exception("Error in tests");
    private ServletRequestAttributes servletRequestAttributes;

    @BeforeEach
    void setup() {
        this.servletRequestAttributes = new ServletRequestAttributes(new MockHttpServletRequest());
    }

    @Test
    void should_CreateMessage_WithPrefixBearer() {
        final String message = UnauthorizedUtil.getWWWAuthenticateMessage(
                this.servletRequestAttributes.getRequest(),
                this.exception
        );

        Assertions.assertTrue(message.startsWith("Bearer"));
    }

    @Test
    void should_CreateMessage_WithRequestUrlInRealmAttribute() {
        final String message = UnauthorizedUtil.getWWWAuthenticateMessage(
                this.servletRequestAttributes.getRequest(),
                this.exception
        );

        final Pattern pattern = Pattern.compile("realm[=][\"]([\\D]+)[\"][,]");
        final Matcher matcher = pattern.matcher(message);

        Assertions.assertTrue(matcher.find());
        Assertions.assertEquals(matcher.group(1),
                this.servletRequestAttributes.getRequest().getRequestURL().toString());
    }

    @Test
    void should_CreateMessage_WithMessageErrorInErrorAttribute() {
        final String message = UnauthorizedUtil.getWWWAuthenticateMessage(
                this.servletRequestAttributes.getRequest(),
                this.exception
        );

        final Pattern pattern = Pattern.compile("error_description[=][\"]([\\D]+)[\"]");
        final Matcher matcher = pattern.matcher(message);

        Assertions.assertTrue(matcher.find());
        Assertions.assertEquals(matcher.group(1), this.exception.getMessage());
    }

    @Test
    void should_CreateMessage_WithNullInErrorAttribute() {
        final Exception exception = new Exception();

        final String message = UnauthorizedUtil.getWWWAuthenticateMessage(
                this.servletRequestAttributes.getRequest(),
                exception
        );

        final Pattern pattern = Pattern.compile("error_description[=][\"]([\\D]+)[\"]");
        final Matcher matcher = pattern.matcher(message);
        matcher.find();

        Assertions.assertEquals(matcher.group(1), "null");
    }
}
