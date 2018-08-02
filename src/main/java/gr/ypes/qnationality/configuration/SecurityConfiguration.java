package gr.ypes.qnationality.configuration;

import gr.ypes.qnationality.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    IUserService userService;

    public SecurityConfiguration() {
        super();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        ExceptionMappingAuthenticationFailureHandler exceptionMappingAuthenticationFailureHandler =
                new ExceptionMappingAuthenticationFailureHandler();
        Map<Object, Object> map = new HashMap<>();
        map.put("org.springframework.security.authentication.CredentialsExpiredException", "/changepass");
        map.put("org.springframework.security.authentication.BadCredentialsException", "/login?error=true");
        map.put("org.springframework.security.authentication.LockedException", "/login?error=locked");

        exceptionMappingAuthenticationFailureHandler.setExceptionMappings(map);

        return exceptionMappingAuthenticationFailureHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/resetpass").fullyAuthenticated()
                .antMatchers("/").fullyAuthenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/moderator/**").hasRole("MODERATOR")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/static").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .passwordParameter("password")
                .failureHandler(customAuthenticationFailureHandler())
                //.and()
                //.logout()
                //.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                //.logoutSuccessUrl("/")
                //.and()
                //.exceptionHandling()
                //.accessDeniedPage("/access-denied")
                ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

}
