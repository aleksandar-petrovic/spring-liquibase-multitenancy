package com.apetrovic.tenants.data.config.tenant.hibernate;

import com.apetrovic.tenants.data.util.TenantContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("currentTenantIdentifierResolver")
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    private final String defaultTenant;

    @Autowired
    public CurrentTenantIdentifierResolverImpl(@Value("${multitenancy.default-tenant:#{null}}") String defaultTenant) {
        this.defaultTenant = defaultTenant;
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getTenantId();
        if (!StringUtils.isEmpty(tenantId)) {
            return tenantId;
        } else if (!StringUtils.isEmpty(this.defaultTenant)) {
            return this.defaultTenant;
        } else {
            throw new IllegalStateException("No tenant selected");
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
