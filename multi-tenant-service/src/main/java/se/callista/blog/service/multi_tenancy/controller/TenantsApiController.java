package se.callista.blog.service.multi_tenancy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.callista.blog.service.multi_tenancy.service.TenantManagementService;

@Controller
@RequestMapping("/")
public class TenantsApiController {

    @Autowired
    private TenantManagementService tenantManagementService;

    @PostMapping("/tenants")
    public ResponseEntity<Void> createTenant(@RequestParam String tenantId) {
        tenantManagementService.createTenant(tenantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
