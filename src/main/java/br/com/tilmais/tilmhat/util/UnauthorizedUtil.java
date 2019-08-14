package br.com.tilmais.tilmhat.util;

import javax.servlet.http.HttpServletRequest;

public class UnauthorizedUtil {

    public static String WWWAuthenticate = "WWW-Authenticate";

    public static String getWWWAuthenticateMessage(HttpServletRequest httpServletRequest, Exception ex) {
        return "Bearer realm=\"" + httpServletRequest.getRequestURL().toString() +
                "\", error_description=\"" + ex.getMessage() + "\"";
    }
}
