package com.apetrovic.tenants.service.multi_tenancy.service;

import com.apetrovic.tenants.service.multi_tenancy.domain.entity.Tenant;
import com.apetrovic.tenants.service.multi_tenancy.repository.TenantRepository;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

@Slf4j
@Service
@EnableConfigurationProperties(LiquibaseProperties.class)
public class TenantManagementServiceImpl implements TenantManagementService {
    private static final String VALID_DATABASE_NAME_REGEXP = "[A-Za-z0-9_]*";

    private final JdbcTemplate jdbcTemplate;
    private final LiquibaseProperties liquibaseProperties;
    private final ResourceLoader resourceLoader;
    private final TenantRepository tenantRepo;

    private final String urlPrefix;
    private final String dbUsername;
    private final String dbPassword;
    private final String liquibaseChangeLog;
    private final String liquibaseContexts;

    @Autowired
    public TenantManagementServiceImpl(JdbcTemplate jdbcTemplate,
                                       @Qualifier("masterLiquibaseProperties")
                                               LiquibaseProperties liquibaseProperties,
                                       ResourceLoader resourceLoader,
                                       TenantRepository tenantRepo,
                                       @Value("${multitenancy.tenant.datasource.url-prefix}") String urlPrefix,
                                       @Value("${multitenancy.tenant.datasource.username}") String dbUsername,
                                       @Value("${multitenancy.tenant.datasource.password}") String dbPassword,
                                       @Value("${multitenancy.tenant.liquibase.changeLog}") String liquibaseChangeLog,
                                       @Value("${multitenancy.tenant.liquibase.contexts:#{null}") String liquibaseContexts
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.liquibaseProperties = liquibaseProperties;
        this.resourceLoader = resourceLoader;
        this.tenantRepo = tenantRepo;
        this.urlPrefix = urlPrefix;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.liquibaseChangeLog = liquibaseChangeLog;
        this.liquibaseContexts = liquibaseContexts;
    }

    @Override
    public void createTenant(String tenantId) {

        // Verify schema string to prevent SQL injection
        if (!tenantId.matches(VALID_DATABASE_NAME_REGEXP)) {
            throw new TenantCreationException("Invalid db name: " + tenantId);
        }

        String url = urlPrefix + tenantId;
        try {
            createDatabase(tenantId);
            runLiquibase(url);
        } catch (DataAccessException e) {
              throw new TenantCreationException("Error when creating schema: " + tenantId, e);
        } catch (LiquibaseException e) {
            throw new TenantCreationException("Error when populating schema: ", e);
        }

        Tenant tenant = Tenant.builder()
                .tenantId(tenantId)
                .url(url)
                .username(dbUsername)
                .password(dbPassword)
                .build();
        tenantRepo.save(tenant);
    }

    private void createDatabase(String dbName) {
        jdbcTemplate.execute((StatementCallback<?>) stmt -> stmt.execute("CREATE DATABASE " + dbName));
        jdbcTemplate.execute((StatementCallback<Boolean>) stmt -> stmt.execute("GRANT ALL PRIVILEGES ON DATABASE " + dbName + " TO " + dbUsername));
    }

    private void runLiquibase(String url) throws LiquibaseException {
        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            DataSource dataSource = new SingleConnectionDataSource(connection, false);
            SpringLiquibase liquibase = getSpringLiquibase(dataSource);
            liquibase.afterPropertiesSet();
        } catch (Exception ex) {
            throw new TenantCreationException("Error while connectiong to tenant db");
        }
    }

    protected SpringLiquibase getSpringLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setResourceLoader(resourceLoader);
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(liquibaseChangeLog);
        liquibase.setContexts(liquibaseContexts);
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        return liquibase;
    }
}
