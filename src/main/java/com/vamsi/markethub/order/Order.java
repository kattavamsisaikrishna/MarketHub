package com.vamsi.markethub.order;

import com.vamsi.markethub.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
public class Order {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",nullable = false)
    private User customer;

    @Getter
    @Setter
    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Getter
    @Setter
    @Column(nullable = false,length = 20)
    private String status;

    @Getter
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> items = new ArrayList<>();

    public Order(){}

    public Order(User customer, BigDecimal totalAmount, String status) {
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
