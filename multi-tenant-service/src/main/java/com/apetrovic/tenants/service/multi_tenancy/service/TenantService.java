package com.apetrovic.tenants.service.multi_tenancy.service;

import org.springframework.data.repository.query.Param;
import com.apetrovic.tenants.service.multi_tenancy.domain.entity.Tenant;

public interface TenantService {

    Tenant findByTenantId(@Param("tenantId") String tenantId);
}
