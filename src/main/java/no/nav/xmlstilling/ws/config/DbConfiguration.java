package no.nav.xmlstilling.ws.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import no.nav.vault.jdbc.hikaricp.HikariCPVaultUtil;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("!dev")
public class DbConfiguration {

    @Value("${database.name}")
    private String dbName;

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${vault.mount-path}")
    private String mountPath;

    @Bean
    public DataSource userDataSource() {
        return dataSource("user");
    }

    @SneakyThrows
    private HikariDataSource dataSource(String user) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseUrl);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        return HikariCPVaultUtil.createHikariDataSourceWithVaultIntegration(config, mountPath, String.format("%s-%s", dbName, user));
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> Flyway.configure()
                .dataSource(dataSource("admin"))
                .initSql(String.format("SET ROLE \"%s-admin\"", dbName))
                .load()
                .migrate();
    }
}
