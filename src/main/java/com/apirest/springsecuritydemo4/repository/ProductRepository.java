package com.apirest.springsecuritydemo4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apirest.springsecuritydemo4.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
    
}
