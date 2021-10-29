package com.apetrovic.tenants.insights.controller;

import com.apetrovic.tenants.data.product.model.ProductValue;
import com.apetrovic.tenants.data.product.service.ProductService;
import com.apetrovic.tenants.data.util.TenantContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/insights")
public class TenantInsightsController {

    private final ProductService productService;

    public TenantInsightsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{tenantId}/products")
    public List<ProductValue> listProductsOfTenant(@PathVariable String tenantId) {
        TenantContext.setTenantId(tenantId);
        return productService.getProducts();
    }
}
