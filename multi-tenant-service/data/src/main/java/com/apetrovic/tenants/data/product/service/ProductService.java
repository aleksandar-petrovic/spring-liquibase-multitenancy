package com.apetrovic.tenants.data.product.service;


import com.apetrovic.tenants.data.product.model.ProductValue;

import java.util.List;

public interface ProductService {

    List<ProductValue> getProducts();

    ProductValue getProduct(long productId);

    ProductValue createProduct(ProductValue productValue);

    ProductValue updateProduct(ProductValue productValue);

    void deleteProductById(long productId);
}
