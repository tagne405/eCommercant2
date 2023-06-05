package com.ecommerce.ecommerce2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce2.Entity.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long>{
    
}
