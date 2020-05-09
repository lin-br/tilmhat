package br.com.tilmais.tilmhat.setting.security;

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
public class MethodNotAllowedFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {

        final String method = request.getMethod();

        if (!method.equals(HttpMethod.POST.name())) {
            throw new MethodNotAllowedException(method, Collections.singleton(HttpMethod.POST));
        }

        filterChain.doFilter(request, response);
    }
}
