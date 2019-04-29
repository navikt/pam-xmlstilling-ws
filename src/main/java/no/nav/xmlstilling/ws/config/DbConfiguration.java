package no.nav.xmlstilling.ws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
//@Profile("!dev")
public class DbConfiguration {

    @Value("${db.name}")
    private String dbName;

    @Bean
    public FlywayConfigurationCustomizer flywayConfig() {
        return c -> c.initSql(String.format("SET ROLE \"%s-admin\"", dbName));
    }
}
