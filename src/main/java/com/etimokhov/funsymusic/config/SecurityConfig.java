package com.etimokhov.funsymusic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .authorizeRequests()
                    .antMatchers("/uploadTrack**")
                    .authenticated()
                .and()
                    .authorizeRequests()
                    .antMatchers("/**")
                    .permitAll()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .failureUrl("/login-error")
                    .defaultSuccessUrl("/home", true)
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/home")
                    .permitAll()
                .and()
                    .csrf().ignoringAntMatchers("/h2-console/**");

        http.headers().frameOptions().disable();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
