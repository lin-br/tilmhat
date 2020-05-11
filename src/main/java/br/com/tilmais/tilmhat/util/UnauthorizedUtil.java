package br.com.tilmais.tilmhat.util;

import javax.servlet.http.HttpServletRequest;

public class UnauthorizedUtil {
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    private UnauthorizedUtil() {
    }

    public static String getWWWAuthenticateMessage(final HttpServletRequest httpServletRequest,
                                                   final Exception ex) {
        return "Bearer realm=\"" + httpServletRequest.getRequestURL().toString() +
                "\", error_description=\"" + ex.getMessage() + "\"";
    }
}
