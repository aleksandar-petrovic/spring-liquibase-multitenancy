package com.apetrovic.tenants.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.apetrovic.tenants.service.domain.entity.Product;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductValue {

    @JsonProperty("productId")
    private Long productId;

    @NotNull
    @Size(max = 255)
    @JsonProperty("name")
    private String name;

    public static ProductValue fromEntity(Product product) {
        return ProductValue.builder()
                .productId(product.getId())
                .name(product.getName())
                .build();
    }

    public static Product fromValue(ProductValue product) {
        return Product.builder()
                .id(product.getProductId())
                .name(product.getName())
                .build();
    }

}
