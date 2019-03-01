package no.nav.xmlstilling.ws;

import no.nav.xmlstilling.ws.web.security.AuthoritiesMapper;
import no.nav.xmlstilling.ws.web.security.NAVLdapUserDetailsMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@Profile("dev")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/isAlive").permitAll()
                .antMatchers("/internal/**").permitAll()
                .antMatchers("/**").hasRole("ROLLE_A")
                .anyRequest().authenticated()
                .and().httpBasic()
                ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder
                .inMemoryAuthentication()
                .withUser("brukerA").password("{noop}pwdA").roles("ROLLE_A").and()
                .withUser("brukerB").password("{noop}pwdB").roles("ROLLE_B");

    }

}
