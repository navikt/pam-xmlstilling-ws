package no.nav.xmlstilling.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Profile({"dev", "test"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/internal/**").permitAll()
                .antMatchers("/**").hasRole("ROLLE_A")
                .anyRequest().authenticated()
                .and().httpBasic();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder
                .inMemoryAuthentication()
                .withUser("brukerA").password("{noop}pwdA").roles("ROLLE_A").and()
                .withUser("karriereno").password("{noop}pwd").roles("ROLLE_A").and()
                .withUser("mynetwork").password("{noop}pwd").roles("ROLLE_A").and()
                .withUser("stepstone").password("{noop}pwd").roles("ROLLE_A").and()
                .withUser("webcruiter").password("{noop}pwd").roles("ROLLE_A").and()
                .withUser("globesoft").password("{noop}pwd").roles("ROLLE_A").and()
                .withUser("hrmanager").password("{noop}pwd").roles("ROLLE_A").and()
                .withUser("jobbnorge").password("{noop}pwd").roles("ROLLE_A").and()
                .withUser("brukerB").password("{noop}pwdB").roles("ROLLE_B");

    }

}
