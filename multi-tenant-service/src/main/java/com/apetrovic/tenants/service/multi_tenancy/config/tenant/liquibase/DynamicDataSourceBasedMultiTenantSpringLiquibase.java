package com.apetrovic.tenants.service.multi_tenancy.config.tenant.liquibase;

import com.apetrovic.tenants.service.multi_tenancy.domain.entity.Tenant;
import com.apetrovic.tenants.service.multi_tenancy.repository.TenantRepository;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Based on MultiTenantSpringLiquibase, this class provides Liquibase support for
 * multi-tenancy based on a dynamic collection of DataSources.
 */
@Getter
@Setter
@Slf4j
public class DynamicDataSourceBasedMultiTenantSpringLiquibase implements InitializingBean, ResourceLoaderAware {

    @Autowired
    private TenantRepository masterTenantRepository;

    private ResourceLoader resourceLoader;
    private String changeLog;
    private String contexts;
    private boolean dropFirst = false;
    private boolean shouldRun = true;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DynamicDataSources based multitenancy enabled");
        this.runOnAllTenants(masterTenantRepository.findAll());
    }

    protected void runOnAllTenants(Collection<Tenant> tenants) {
        for(Tenant tenant : tenants) {
            log.info("Initializing Liquibase for tenant " + tenant.getTenantId());
            try (Connection connection = DriverManager.getConnection(tenant.getUrl(), tenant.getUsername(), tenant.getPassword())) {
                DataSource tenantDataSource = new SingleConnectionDataSource(connection, false);
                SpringLiquibase liquibase = this.getSpringLiquibase(tenantDataSource);
                liquibase.afterPropertiesSet();
            } catch (SQLException | LiquibaseException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            log.info("Liquibase ran for tenant " + tenant.getTenantId());
        }
    }

    protected SpringLiquibase getSpringLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setResourceLoader(getResourceLoader());
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(getChangeLog());
        liquibase.setContexts(getContexts());
        liquibase.setDropFirst(isDropFirst());
        liquibase.setShouldRun(isShouldRun());
        return liquibase;
    }

}
