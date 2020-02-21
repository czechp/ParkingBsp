package com.company.Parking.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        User user1 = new User("admin", getPasswordEncoder().encode("admin"), Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        User user2 = new User("user", getPasswordEncoder().encode("user"), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));


        auth.inMemoryAuthentication().withUser(user1);
        auth.inMemoryAuthentication().withUser(user2);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**").authenticated()
                .antMatchers("/car_delete_details_deleted", "/car_delete_all").hasRole("ADMIN")
                .and()
                .formLogin().permitAll()
                .and()
                .logout();

    }
}
