package com.apetrovic.tenants.service.multi_tenancy.service;

public class TenantCreationException extends RuntimeException {

    public TenantCreationException(String message) {
        super(message);
    }

    public TenantCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TenantCreationException(Throwable cause) {
        super(cause);
    }
}
