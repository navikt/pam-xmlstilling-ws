package no.nav.xmlstilling.ws.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import no.nav.vault.jdbc.hikaricp.HikariCPVaultUtil;
import no.nav.vault.jdbc.hikaricp.VaultError;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile({"!dev & !test"})
public class DbConfiguration {

    @Value("${database.name}")
    private String dbName;

    @Value("${database.url}")
    private String databaseUrl;

    @Value("${vault.mount-path}")
    private String mountPath;

    @Bean
    public DataSource userDataSource() throws VaultError {
        return dataSource("user");
    }

    private HikariDataSource dataSource(String user) throws VaultError {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseUrl);
        config.setMaximumPoolSize(2);
        config.setMinimumIdle(1);
        return HikariCPVaultUtil.createHikariDataSourceWithVaultIntegration(config, mountPath, String.format("%s-%s", dbName, user));
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() throws VaultError {
        DataSource dataSource = dataSource("admin");
        return flyway -> Flyway.configure()
                .dataSource(dataSource)
                .initSql(String.format("SET ROLE \"%s-admin\"", dbName))
                .load()
                .migrate();
    }
}
