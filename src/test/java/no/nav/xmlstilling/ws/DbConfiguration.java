package no.nav.xmlstilling.ws;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("dev")
public class DbConfiguration {

    @Value("${database.name}")
    private String dbName;

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    @Bean
    @Primary
    public DataSource userDataSource() {
        return dataSource();
    }

    private HikariDataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(2);
        config.setMinimumIdle(1);
        return new HikariDataSource(config);
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> Flyway.configure()
                .dataSource(dataSource())
                .initSql(databaseUrl.contains("postgresql") ? String.format("SET ROLE \"%s-admin\"", dbName) : "")
                .load()
                .migrate();
    }
}
