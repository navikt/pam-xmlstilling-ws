package no.nav.xmlstilling.ws.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Profile("!dev")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static transient Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Value("${ldap.domain}")
    private String ldapDomain;

    @Value("${ldap.url}")
    private String ldapUrl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/isAlive").permitAll()
                //.antMatchers("/internal/**").permitAll()
                .antMatchers("/internal/**").hasRole("ROLLE_A")
                //.antMatchers("/**").hasRole("EKSTERNBRUKER")
                .antMatchers("/**").hasRole("ROLLE_A")
                .anyRequest().authenticated()
                .and().httpBasic()
                ;
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
//        builder
//                .authenticationProvider(activeDirectoryLdapAuthenticationProvider())
//                .userDetailsService(userDetailsService());
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder
                .inMemoryAuthentication()
                .withUser("brukerA").password("{noop}pwdA").roles("ROLLE_A").and()
                .withUser("brukerB").password("{noop}pwdB").roles("ROLLE_B");

    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProvider()));
    }

    @Bean
    public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(ldapDomain, ldapUrl);
        provider.setAuthoritiesMapper(new AuthoritiesMapper());
        provider.setUserDetailsContextMapper(new NAVLdapUserDetailsMapper());
        provider.setUseAuthenticationRequestCredentials(true);
        provider.setConvertSubErrorCodesToExceptions(true);
        return provider;
    }
}
