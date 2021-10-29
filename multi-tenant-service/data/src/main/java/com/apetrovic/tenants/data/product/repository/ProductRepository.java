package com.apetrovic.tenants.data.product.repository;

import com.apetrovic.tenants.data.product.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
