package br.com.tilmais.tilmhat;

import br.com.tilmais.tilmhat.setting.ApplicationConstants;
import br.com.tilmais.tilmhat.setting.ApplicationProperties;
import br.com.tilmais.tilmhat.setting.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserAuthenticationProvider userAuthenticationProvider;
    private MethodNotAlloedFilter methodNotAlloedFilter;
    private ApplicationProperties applicationProperties;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private ExceptionHandlerFilter exceptionHandlerFilter;

    @Autowired
    public ApplicationSecurityConfig(UserAuthenticationProvider userAuthenticationProvider,
                                     MethodNotAlloedFilter methodNotAlloedFilter,
                                     JwtAuthenticationFilter jwtAuthenticationFilter,
                                     ExceptionHandlerFilter exceptionHandlerFilter,
                                     ApplicationProperties applicationProperties) {
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.methodNotAlloedFilter = methodNotAlloedFilter;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
        this.applicationProperties = applicationProperties;
    }

    @Bean
    LoginFilter getLoginFilter() throws Exception {
        return new LoginFilter(this.authenticationManager(), this.applicationProperties);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.userAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(this.exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(this.methodNotAlloedFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(this.getLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, ApplicationConstants.AUTHENTICATION_PATH).permitAll()
                .anyRequest()
                .authenticated();
    }
}
