package com.vamsi.markethub.product;

import com.vamsi.markethub.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Get all products owned by a seller
    List<Product> findBySeller(User seller);

    // Search products by name for a seller
    List<Product> findBySellerAndNameContainingIgnoreCase(User seller, String name);
}
