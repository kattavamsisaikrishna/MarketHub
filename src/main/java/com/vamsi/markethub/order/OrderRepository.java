package com.vamsi.markethub.order;

import com.vamsi.markethub.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Get all orders of a customer
    List<Order> findByCustomerOrderByCreatedAtDesc(User customer);
}
