package com.apetrovic.tenants.service.repository;

import com.apetrovic.tenants.service.domain.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
