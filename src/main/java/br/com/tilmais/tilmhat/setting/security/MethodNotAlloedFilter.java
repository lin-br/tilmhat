package br.com.tilmais.tilmhat.setting.security;

import br.com.tilmais.tilmhat.setting.ApplicationConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.MethodNotAllowedException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class MethodNotAlloedFilter extends OncePerRequestFilter {

    @Value("${server.servlet.context-path}")
    private String path;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String method = request.getMethod();
        final String requestURI = request.getRequestURI();

        if (requestURI.equals(this.path.concat(ApplicationConstants.AUTHENTICATION_PATH)) &&
                !method.equals(HttpMethod.POST.name())) {
            throw new MethodNotAllowedException(method, Collections.singleton(HttpMethod.POST));
        }

        filterChain.doFilter(request, response);
    }
}
