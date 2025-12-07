package com.vamsi.markethub.product;

import com.vamsi.markethub.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="products")
public class Product {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id",nullable = false)
    private User seller;

    @Getter
    @Setter
    @Column(nullable = false,length = 100)
    private String name;

    @Getter
    @Setter
    @Column(length = 300)
    private String description;

    @Getter
    @Setter
    @Column(nullable = false)
    private BigDecimal price;

    @Getter
    @Setter
    @Column(nullable = false)
    private Integer stockQuantity;

    @Getter
    @Setter
    @Column(length = 50)
    private String category;

    @Getter
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Getter
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Product(){}

    public Product(User seller, String name, String description,
                   BigDecimal price, Integer stockQuantity, String category) {
        this.seller = seller;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
    }

}
