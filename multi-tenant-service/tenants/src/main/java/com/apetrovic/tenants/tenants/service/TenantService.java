package com.apetrovic.tenants.tenants.service;

import com.apetrovic.tenants.tenants.entity.Tenant;
import org.springframework.data.repository.query.Param;

public interface TenantService {

    Tenant findByTenantId(@Param("tenantId") String tenantId);
}
